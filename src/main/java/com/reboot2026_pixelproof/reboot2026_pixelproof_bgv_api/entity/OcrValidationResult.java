package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity;

import lombok.Data;

@Data
public class OcrValidationResult {
    private String id;

    private String documentId;
    private String extractedText;
    private String validationStatus;

}

