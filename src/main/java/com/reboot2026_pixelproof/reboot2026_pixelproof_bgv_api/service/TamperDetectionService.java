package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.service;

import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.TamperAnalysisResult;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.TamperResponse;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.repository.TamperAnalysisResultRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.io.IOException;
import java.util.*;

@Service
public class TamperDetectionService {
    private final TamperAnalysisResultRepository repo;

    public TamperDetectionService(TamperAnalysisResultRepository repo) {
        this.repo = repo;
    }

    public TamperResponse getTamperResult(String documentId) throws InterruptedException {
        return repo.findByDocumentId(documentId)
                .map(result -> new TamperResponse(
                        result.getFindings(),
                        result.getConfidenceScore(),
                        result.getStatus()
                ))
                .orElse(null);
    }

    // Run tamper analysis using Vision API
    public TamperResponse analyze(MultipartFile file, String documentId) {
        List<String> findings = new ArrayList<>();
        int confidence = 90; // default baseline

        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
            ByteString imgBytes = ByteString.readFrom(file.getInputStream());
            Image img = Image.newBuilder().setContent(imgBytes).build();

            // Detect logos, text, and objects
            Feature logoDetection = Feature.newBuilder().setType(Feature.Type.LOGO_DETECTION).build();
            Feature textDetection = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
            Feature objectDetection = Feature.newBuilder().setType(Feature.Type.OBJECT_LOCALIZATION).build();

            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(logoDetection)
                    .addFeatures(textDetection)
                    .addFeatures(objectDetection)
                    .setImage(img)
                    .build();

            AnnotateImageResponse response = vision.batchAnnotateImages(Collections.singletonList(request))
                    .getResponses(0);

            if (response.hasError()) {
                throw new RuntimeException("Vision API error: " + response.getError().getMessage());
            }

            // Logo findings
            for (EntityAnnotation logo : response.getLogoAnnotationsList()) {
                findings.add("Detected logo: " + logo.getDescription());
            }

            // Text anomalies
            if (response.getTextAnnotationsCount() > 0) {
                findings.add("Text detected: " + response.getTextAnnotations(0).getDescription());
            }

            // Object anomalies
            for (LocalizedObjectAnnotation obj : response.getLocalizedObjectAnnotationsList()) {
                findings.add("Object detected: " + obj.getName());
            }

            String status = findings.isEmpty() ? "NO_ANOMALY" : "ANOMALY_CHECK_COMPLETED";

            TamperAnalysisResult entity = new TamperAnalysisResult();
            entity.setDocumentId(documentId);
            entity.setFindings(findings);
            entity.setConfidenceScore(confidence);
            entity.setStatus(status);
            repo.save(entity);

            return new TamperResponse(findings, confidence, status);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file for tamper analysis", e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}


