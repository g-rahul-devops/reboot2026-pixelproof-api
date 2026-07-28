package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditTrailEvent {
    private String id;

    private String documentReferenceId;
    private String stageName;
    private String status;
    private LocalDateTime timestamp;
    private String sha256Fingerprint;
    private String resultSummary;
    private String riskIndicators;
    private String previousAuditHash;
}

