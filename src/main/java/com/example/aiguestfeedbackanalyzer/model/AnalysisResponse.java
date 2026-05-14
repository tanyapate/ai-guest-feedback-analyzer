package com.example.aiguestfeedbackanalyzer.model;

public class AnalysisResponse {

    private String text;
    private String summary;

    public AnalysisResponse() {
    }

    public AnalysisResponse(String text, String summary) {
        this.text = text;
        this.summary = summary;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
