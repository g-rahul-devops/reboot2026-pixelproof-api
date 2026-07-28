package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditTrailResponse {

    private String documentReferenceId;
    private String stageName;
    private String status;
    private LocalDateTime timestamp;
    private String sha256Fingerprint;
    private String resultSummary;
    private String riskIndicators;
    private String previousAuditHash;

    public AuditTrailResponse() {}

    public AuditTrailResponse(String documentReferenceId, String stageName, String status,
                              LocalDateTime timestamp, String sha256Fingerprint,
                              String resultSummary, String riskIndicators, String previousAuditHash) {
        this.documentReferenceId = documentReferenceId;
        this.stageName = stageName;
        this.status = status;
        this.timestamp = timestamp;
        this.sha256Fingerprint = sha256Fingerprint;
        this.resultSummary = resultSummary;
        this.riskIndicators = riskIndicators;
        this.previousAuditHash = previousAuditHash;
    }
}

