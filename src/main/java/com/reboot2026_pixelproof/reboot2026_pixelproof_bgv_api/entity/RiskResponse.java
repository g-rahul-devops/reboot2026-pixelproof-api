package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity;

import lombok.Data;

import java.util.List;
@Data
public class RiskResponse {
    private int score;
    private String decision;
    private List<String> reasonCodes;

    public RiskResponse() {}

    public RiskResponse(int score, String decision, List<String> reasonCodes) {
        this.score = score;
        this.decision = decision;
        this.reasonCodes = reasonCodes;
    }

    // getters and setters
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }

    public List<String> getReasonCodes() { return reasonCodes; }
    public void setReasonCodes(List<String> reasonCodes) { this.reasonCodes = reasonCodes; }
}

