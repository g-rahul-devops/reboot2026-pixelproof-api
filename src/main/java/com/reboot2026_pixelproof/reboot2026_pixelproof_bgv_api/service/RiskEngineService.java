package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.service;

import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.*;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.repository.RiskScoreResultRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class RiskEngineService {
    private final RiskScoreResultRepository repo;

    public RiskEngineService(RiskScoreResultRepository repo) {
        this.repo = repo;
    }

    public RiskResponse calculateRisk(String documentId,
                                      DocumentMetadata metadata,
                                      OcrResponse ocr,
                                      DocumentAnalysisResponse tamper) throws InterruptedException {
        int score = 0;
        List<String> reasons = new ArrayList<>();

        // Metadata checks
        if (metadata.getMimeType() != null && metadata.getMimeType().contains("image")) {
            score += 20;
            reasons.add("Document is an image file, higher tamper risk");
        }

        // OCR checks
        if (ocr.getValidationStatus().equals("OCR_FAILED")) {
            score += 30;
            reasons.add("OCR failed, content unreadable");
        }

        // Tamper findings
        if (tamper.isTampered()) {
            score += 40;
            reasons.add("Tamper anomalies detected");
        }

        // Decision
        String decision;
        if (score < 30) {
            decision = "LOW_RISK";
        } else if (score < 70) {
            decision = "MEDIUM_RISK";
        } else {
            decision = "HIGH_RISK";
        }

        RiskScoreResult entity = new RiskScoreResult();
        entity.setDocumentId(documentId);
        entity.setScore(score);
        entity.setDecision(decision);
        entity.setReasonCodes(String.join(",", reasons));
        repo.save(entity);

        return new RiskResponse(score, decision, reasons);
    }

    public RiskResponse getRiskResult(String documentId) throws InterruptedException {
        return repo.findByDocumentId(documentId)
                .map(r -> new RiskResponse(
                        r.getScore(),
                        r.getDecision(),
                        List.of(r.getReasonCodes().split(","))
                ))
                .orElse(null);
    }
}


