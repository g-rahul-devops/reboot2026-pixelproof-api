package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity;

import lombok.Data;

@Data
public class UploadResponse {
    private String documentId;
    private String fingerprint;
    private String decision;

    public UploadResponse(String documentId, String fingerprint, String decision) {
        this.documentId = documentId;
        this.fingerprint = fingerprint;
        this.decision = decision;
    }
}

