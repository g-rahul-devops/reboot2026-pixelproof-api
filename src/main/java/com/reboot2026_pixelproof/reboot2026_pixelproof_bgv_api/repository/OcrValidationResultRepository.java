package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.repository;

import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.OcrValidationResult;

import java.util.List;
import java.util.Optional;

public interface OcrValidationResultRepository{
    Optional<OcrValidationResult> findByDocumentId(String documentId) throws InterruptedException;
    List<OcrValidationResult> findAll() throws InterruptedException;
    void save(OcrValidationResult ocrValidationResult) throws InterruptedException;
    void deleteById(String id) throws InterruptedException;
}

