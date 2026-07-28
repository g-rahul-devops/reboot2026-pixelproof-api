package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.longrunning.OperationFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.*;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.DocumentRecord;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.OcrResponse;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.OcrValidationResult;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.repository.BigQueryDocumentRepository;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.repository.OcrValidationResultRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class OcrService {
    private final BigQueryDocumentRepository repo;

    public OcrService(BigQueryDocumentRepository repo) {
        this.repo = repo;
    }


    // Run OCR using Google Vision API and persist results
    public OcrResponse extractText(MultipartFile file, String documentId) throws IOException {
        // Validate file type
        if (!List.of("application/pdf", "image/png", "image/jpeg").contains(file.getContentType())) {
            throw new RuntimeException("Unsupported file type: " + file.getContentType());
        }

        // Validate file size
        if (file.getSize() == 0) {
            throw new RuntimeException("File is empty.");
        }

        // Load credentials
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("src/main/resources/ltc-hack2026-team27.json"));
        ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create(settings)) {
            ByteString fileBytes = ByteString.readFrom(file.getInputStream());

            if ("application/pdf".equals(file.getContentType())) {
                // Handle PDF files
                InputConfig inputConfig = InputConfig.newBuilder()
                        .setMimeType("application/pdf")
                        .setContent(fileBytes)
                        .build();

                OutputConfig outputConfig = OutputConfig.newBuilder()
                        .setGcsDestination(GcsDestination.newBuilder().setUri("gs://pixelproof-dev/output/"))
                        .setBatchSize(1)
                        .build();

                AsyncAnnotateFileRequest asyncFileRequest = AsyncAnnotateFileRequest.newBuilder()
                        .addFeatures(Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build())
                        .setInputConfig(inputConfig)
                        .setOutputConfig(outputConfig)
                        .build();

                AsyncBatchAnnotateFilesRequest asyncBatchRequest = AsyncBatchAnnotateFilesRequest.newBuilder()
                        .addRequests(asyncFileRequest)
                        .build();

                OperationFuture<AsyncBatchAnnotateFilesResponse, OperationMetadata> future =
                        vision.asyncBatchAnnotateFilesAsync(asyncBatchRequest);

                // Wait for the operation to complete
                future.get();

                return new OcrResponse("Processing PDF asynchronously", "OCR_IN_PROGRESS");
            } else {
                // Handle image files
                Image img = Image.newBuilder().setContent(fileBytes).build();
                AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                        .addFeatures(Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build())
                        .setImage(img)
                        .build();

                AnnotateImageResponse response = vision.batchAnnotateImages(Collections.singletonList(request))
                        .getResponses(0);

                if (response.hasError()) {
                    throw new RuntimeException("Vision API error: " + response.getError().getMessage());
                }

                String extractedText = response.getFullTextAnnotation().getText();
                String status = extractedText != null && !extractedText.isEmpty()
                        ? "OCR_COMPLETED"
                        : "OCR_FAILED";

                DocumentRecord entity = new DocumentRecord();
                entity.setDocument_id(documentId);
                entity.setOcr_validation_status(status);
                repo.updateOcrSave(entity);

                return new OcrResponse(extractedText, status);
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error reading file for OCR", e);
        }
    }
}


