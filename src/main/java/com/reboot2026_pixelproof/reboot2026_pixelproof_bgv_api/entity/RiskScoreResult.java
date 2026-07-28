package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity;

import lombok.Data;

@Data
public class RiskScoreResult {
    private String id;

    private String documentId;
    private int score;
    private String decision;
    private String reasonCodes;
}

