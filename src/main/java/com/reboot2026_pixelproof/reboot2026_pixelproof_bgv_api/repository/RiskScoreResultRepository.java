package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.repository;

import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.RiskScoreResult;

import java.util.Optional;
import java.util.List;

public interface RiskScoreResultRepository {
    Optional<RiskScoreResult> findByDocumentId(String documentId) throws InterruptedException;
    List<RiskScoreResult> findAll() throws InterruptedException;
    void save(RiskScoreResult riskScoreResult) throws InterruptedException;
    void deleteById(String id) throws InterruptedException;
}