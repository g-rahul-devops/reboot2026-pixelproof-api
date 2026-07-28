package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity;

import lombok.Data;

import java.util.List;
@Data
public class TamperResponse {
    private List<String> findings;
    private int confidenceScore;
    private String status;

    public TamperResponse() {}

    public TamperResponse(List<String> findings, int confidenceScore, String status) {
        this.findings = findings;
        this.confidenceScore = confidenceScore;
        this.status = status;
    }

    public List<String> getFindings() { return findings; }
    public void setFindings(List<String> findings) { this.findings = findings; }

    public int getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(int confidenceScore) { this.confidenceScore = confidenceScore; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

