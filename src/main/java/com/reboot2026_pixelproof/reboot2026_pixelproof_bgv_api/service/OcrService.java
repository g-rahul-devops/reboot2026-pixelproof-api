package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.service;

import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.OcrResponse;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.OcrValidationResult;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.repository.OcrValidationResultRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.Feature;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;

@Service
public class OcrService {
    private final OcrValidationResultRepository repo;

    public OcrService(OcrValidationResultRepository repo) {
        this.repo = repo;
    }

    public OcrResponse getOcrResult(String documentId) throws InterruptedException {
        return repo.findByDocumentId(documentId)
                .map(result -> new OcrResponse(
                        result.getExtractedText(),
                        result.getValidationStatus()
                ))
                .orElse(null);
    }

    // Run OCR using Google Vision API and persist results
    public OcrResponse extractText(MultipartFile file, String documentId) {
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
            ByteString imgBytes = ByteString.readFrom(file.getInputStream());

            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build();
            AnnotateImageRequest request =
                    AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();

            AnnotateImageResponse response = vision.batchAnnotateImages(Collections.singletonList(request))
                    .getResponses(0);

            if (response.hasError()) {
                throw new RuntimeException("Vision API error: " + response.getError().getMessage());
            }

            String extractedText = response.getFullTextAnnotation().getText();
            String status = extractedText != null && !extractedText.isEmpty()
                    ? "OCR_COMPLETED"
                    : "OCR_FAILED";

            OcrValidationResult entity = new OcrValidationResult();
            entity.setDocumentId(documentId);
            entity.setExtractedText(extractedText);
            entity.setValidationStatus(status);
            repo.save(entity);

            return new OcrResponse(extractedText, status);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file for OCR", e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}


