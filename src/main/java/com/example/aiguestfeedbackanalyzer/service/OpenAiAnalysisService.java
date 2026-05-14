package com.example.aiguestfeedbackanalyzer.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OpenAiAnalysisService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenAiAnalysisService.class);
    private static final String RESPONSES_API_URL = "https://api.openai.com/v1/responses";
    private static final String ANALYSIS_INSTRUCTIONS = """
            Analyze the guest review and return the result in this exact format:

            Summary:
            One concise sentence summarizing the guest's overall experience.

            Sentiment:
            Positive, Negative, Mixed, or Neutral. Include a brief reason.

            Key Issues:
            - List the main actionable issues mentioned by the guest.
            - If there are no actionable issues, write "- None".

            Keep the response clear, practical, and easy for hospitality staff to scan.
            """;

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String model;

    public OpenAiAnalysisService(
            RestTemplate restTemplate,
            @Value("${openai.api.key:}") String apiKey,
            @Value("${openai.model:gpt-5}") String model) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.model = model;
    }

    public String analyzeReview(String reviewText) {
        if (apiKey == null || apiKey.isBlank()) {
            LOGGER.warn("OpenAI API key is not configured.");
            return unavailableAnalysis("OpenAI API key is not configured.");
        }

        String text = reviewText == null ? "" : reviewText;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "instructions", ANALYSIS_INSTRUCTIONS,
                "input", text
        );

        try {
            JsonNode response = restTemplate.postForObject(
                    RESPONSES_API_URL,
                    new HttpEntity<>(requestBody, headers),
                    JsonNode.class
            );

            String outputText = extractOutputText(response);
            if (outputText.isBlank()) {
                LOGGER.warn("OpenAI response did not include output text.");
                return unavailableAnalysis("The AI response was empty.");
            }

            return outputText;
        } catch (HttpStatusCodeException exception) {
            LOGGER.warn("OpenAI request failed with status {}.", exception.getStatusCode(), exception);
            return unavailableAnalysis("OpenAI returned " + exception.getStatusCode() + ".");
        } catch (RestClientException exception) {
            LOGGER.warn("OpenAI request failed.", exception);
            return unavailableAnalysis("The AI request could not be completed.");
        }
    }

    private String unavailableAnalysis(String reason) {
        return """
                Summary:
                Analysis is currently unavailable.

                Sentiment:
                Neutral. The review could not be analyzed automatically.

                Key Issues:
                - %s
                """.formatted(reason);
    }

    private String extractOutputText(JsonNode response) {
        if (response == null) {
            return "";
        }

        JsonNode outputText = response.path("output_text");
        if (outputText.isTextual()) {
            return outputText.asText();
        }

        JsonNode output = response.path("output");
        if (output.isArray()) {
            StringBuilder text = new StringBuilder();
            for (JsonNode item : output) {
                JsonNode content = item.path("content");
                if (!content.isArray()) {
                    continue;
                }
                for (JsonNode contentItem : content) {
                    JsonNode contentText = contentItem.path("text");
                    if (contentText.isTextual()) {
                        text.append(contentText.asText());
                    }
                }
            }
            return text.toString();
        }

        return "";
    }
}
