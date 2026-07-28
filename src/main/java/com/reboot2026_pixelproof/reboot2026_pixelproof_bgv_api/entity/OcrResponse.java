package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity;

import lombok.Data;

@Data
public class OcrResponse {
    private String extractedText;
    private String validationStatus;

    public OcrResponse() {}

    public OcrResponse(String extractedText, String validationStatus) {
        this.extractedText = extractedText;
        this.validationStatus = validationStatus;
    }

    // getters and setters
    public String getExtractedText() { return extractedText; }
    public void setExtractedText(String extractedText) { this.extractedText = extractedText; }

    public String getValidationStatus() { return validationStatus; }
    public void setValidationStatus(String validationStatus) { this.validationStatus = validationStatus; }
}

