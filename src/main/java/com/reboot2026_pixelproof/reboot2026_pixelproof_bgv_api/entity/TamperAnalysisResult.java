package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity;

import lombok.Data;

import java.util.List;

@Data
public class TamperAnalysisResult {
    private String id;

    private String documentId;

    private List<String> findings;

    private int confidenceScore;
    private String status;

}

