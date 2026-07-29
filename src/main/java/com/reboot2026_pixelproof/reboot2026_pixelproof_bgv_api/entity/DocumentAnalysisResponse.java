package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DocumentAnalysisResponse {
    private String documentType;
    private boolean tampered;
    private Integer confidence;
    private Integer riskScore;
    private List<String> reasons;
}
