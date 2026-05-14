package com.example.aiguestfeedbackanalyzer.service;

import com.example.aiguestfeedbackanalyzer.model.AnalysisRequest;
import com.example.aiguestfeedbackanalyzer.model.AnalysisResponse;
import org.springframework.stereotype.Service;

@Service
public class AnalysisService {

    public AnalysisResponse analyze(AnalysisRequest request) {
        String text = request == null ? "" : request.getText();
        String summary = text == null || text.isBlank()
                ? "No feedback text provided."
                : "Feedback received for analysis.";

        return new AnalysisResponse(text, summary);
    }
}
