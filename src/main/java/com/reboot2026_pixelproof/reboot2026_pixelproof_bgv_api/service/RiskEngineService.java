package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.service;

import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.*;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.repository.BigQueryDocumentRepository;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.repository.RiskScoreResultRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class RiskEngineService {
    private final BigQueryDocumentRepository repo;

    public RiskEngineService(BigQueryDocumentRepository repo) {
        this.repo = repo;
    }

    public RiskResponse calculateRisk(String documentId,
                                      DocumentMetadata metadata,
                                      DocumentAnalysisResponse tamper) throws InterruptedException {
        int score = 0;
        List<String> reasons = new ArrayList<>();

        // Metadata checks
        if (metadata.getMimeType() != null && metadata.getMimeType().contains("image")) {
            score += 20;
            reasons.add("Document is an image file, higher tamper risk");
        }

        // Tamper findings
        if (tamper.isTampered()) {
            score += 70;
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

        DocumentRecord entity = new DocumentRecord();
        entity.setDocument_id(documentId);
        entity.setScore(score);
        entity.setDecision(decision);
        entity.setReasonCodes(String.join(",", reasons));
        repo.updateOcrSave(entity);

        return new RiskResponse(score, decision, reasons);
    }

}


