package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.repository;

import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.TamperAnalysisResult;

import java.util.Optional;
import java.util.List;

public interface TamperAnalysisResultRepository {
    Optional<TamperAnalysisResult> findByDocumentId(String documentId) throws InterruptedException;
    List<TamperAnalysisResult> findAll() throws InterruptedException;
    void save(TamperAnalysisResult tamperAnalysisResult) throws InterruptedException;
    void deleteById(String id) throws InterruptedException;
}