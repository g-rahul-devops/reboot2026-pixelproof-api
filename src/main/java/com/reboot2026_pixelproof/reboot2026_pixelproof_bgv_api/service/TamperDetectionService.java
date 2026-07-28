package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.PartMaker;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.config.VertexAiProperties;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.DocumentAnalysisResponse;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.TamperResponse;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.repository.TamperAnalysisResultRepository;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.util.PromptTemplates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TamperDetectionService {
    private final TamperAnalysisResultRepository repo;
    private final VertexAiProperties vertexAiProperties;
    private final ObjectMapper objectMapper;


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
    public DocumentAnalysisResponse analyzeDocument(String gcsUri) {

        try (VertexAI vertexAI = new VertexAI(vertexAiProperties.getProjectId(), vertexAiProperties.getLocation())) {

            // Configure model with system instructions
            GenerativeModel model = new GenerativeModel(vertexAiProperties.getModel(), vertexAI)
                    .withSystemInstruction(ContentMaker.fromString(PromptTemplates.DOCUMENT_ANALYSIS));

            // Create request
            GenerateContentResponse response = model.generateContent(
                    ContentMaker.fromMultiModalData(
                            "Analyse this uploaded document. Return JSON only.",
                            PartMaker.fromMimeTypeAndData("application/pdf", gcsUri)
                    )
            );

            // Read response
            String json = ResponseHandler.getText(response);

            // Convert to DTO
            return objectMapper.readValue(json, DocumentAnalysisResponse.class);

        } catch (Exception e) {
            throw new RuntimeException("Document analysis failed", e);
        }
    }
}


