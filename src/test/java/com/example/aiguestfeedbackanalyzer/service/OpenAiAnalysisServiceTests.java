package com.example.aiguestfeedbackanalyzer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

class OpenAiAnalysisServiceTests {

    @Test
    void returnsFallbackWhenApiKeyIsMissing() {
        OpenAiAnalysisService service = new OpenAiAnalysisService(mock(RestTemplate.class), "", "gpt-5");

        String result = service.analyzeReview("The room was clean.");

        assertThat(result).contains("Summary:");
        assertThat(result).contains("Analysis is currently unavailable.");
        assertThat(result).contains("OpenAI API key is not configured.");
    }

    @Test
    void returnsFallbackWhenOpenAiRequestFails() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(JsonNode.class)))
                .thenThrow(new ResourceAccessException("Connection timed out"));
        OpenAiAnalysisService service = new OpenAiAnalysisService(restTemplate, "test-api-key", "gpt-5");

        String result = service.analyzeReview("The room was clean.");

        assertThat(result).contains("Summary:");
        assertThat(result).contains("Analysis is currently unavailable.");
        assertThat(result).contains("The AI request could not be completed.");
    }
}
