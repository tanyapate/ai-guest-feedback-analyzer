package com.example.aiguestfeedbackanalyzer.controller;

import com.example.aiguestfeedbackanalyzer.model.ReviewRequest;
import com.example.aiguestfeedbackanalyzer.service.OpenAiAnalysisService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AnalysisController {

    private final OpenAiAnalysisService openAiAnalysisService;

    public AnalysisController(OpenAiAnalysisService openAiAnalysisService) {
        this.openAiAnalysisService = openAiAnalysisService;
    }

    @PostMapping("/analyze")
    public String analyze(@RequestBody ReviewRequest request) {
        String reviewText = request == null ? "" : request.getText();
        return openAiAnalysisService.analyzeReview(reviewText);
    }
}
