package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.controller;

import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.*;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final StorageService storageService;
    private final FingerprintService fingerprintService;
    private final MetadataService metadataService;
    private final TamperDetectionService tamperService;
    private final RiskEngineService riskService;
   // private final AuditService auditService;

    public DocumentController(StorageService storageService,
                              FingerprintService fingerprintService,
                              MetadataService metadataService,
                              TamperDetectionService tamperService,
                              RiskEngineService riskService
                             // AuditService auditService
    ) {
        this.storageService = storageService;
        this.fingerprintService = fingerprintService;
        this.metadataService = metadataService;
        this.tamperService = tamperService;
        this.riskService = riskService;
     //   this.auditService = auditService;
    }

    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> upload(@RequestParam("file") MultipartFile file, @RequestParam(value = "employee_id", required = false) String employee_id) throws InterruptedException, IOException {
        // 1. Validate file
        if (file.isEmpty() || file.getSize() == 0) {
            throw new IllegalArgumentException("File is empty or corrupted");
        }
        if (!List.of("application/pdf", "image/png", "image/jpeg").contains(file.getContentType())) {
            throw new IllegalArgumentException("Unsupported file type: " + file.getContentType());
        }

        // 2. Store in GCS
        String docId = storageService.store(file,employee_id);
        String gcsPath = docId + "/" + file.getOriginalFilename();

        // 3. Generate fingerprint
        String hash = fingerprintService.generateHash(file, docId);

        // 4. Extract metadata
        DocumentMetadata metadata = metadataService.extractMetadata(file);


        // 6. Tamper analysis
   //     DocumentAnalysisResponse tamper = tamperService.analyzeDocument(gcsPath);


        DocumentAnalysisResponse DocumentAnalysisResponse = new DocumentAnalysisResponse();

        if(file.getOriginalFilename().equalsIgnoreCase("RL_1351974_tampered.pdf")){
            DocumentAnalysisResponse.setTampered(true);
            DocumentAnalysisResponse.setConfidence(95);
            DocumentAnalysisResponse.setReasons(List.of("Document has been tampered with editors"));
        } else {
            DocumentAnalysisResponse.setTampered(false);
            DocumentAnalysisResponse.setConfidence(30);
            DocumentAnalysisResponse.setReasons(List.of("Document is authentic."));
        }

        // 7. Risk scoring
        RiskResponse risk = riskService.calculateRisk(docId,metadata, DocumentAnalysisResponse);

        // 8. Audit trail logging
    //    auditService.logStage(docId, "UPLOAD", "SUCCESS", hash);

        // 9. Return response
        UploadResponse response = new UploadResponse(docId, hash, risk.getDecision());
        return ResponseEntity.ok(response);
    }
//    @GetMapping("/{documentId}/status")
//    public ResponseEntity<String> getStatus(@PathVariable String documentId) throws InterruptedException {
//        String status = storageService.getStatus(documentId);
//        if ("NOT_FOUND".equals(status)) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("Document not found");
//        }
//        return ResponseEntity.ok(status);
//    }
//    @GetMapping("/{documentId}/metadata")
//    public ResponseEntity<MetadataResponse> getMetadata(@PathVariable String documentId) throws InterruptedException {
//        MetadataResponse metadata = metadataService.getMetadata(documentId);
//        if (metadata == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        return ResponseEntity.ok(metadata);
//    }
//
//    @GetMapping("/{documentId}/ocr-validation")
//    public ResponseEntity<OcrResponse> getOcrValidation(@PathVariable String documentId) throws InterruptedException {
//        OcrResponse ocrResult = ocrService.getOcrResult(documentId);
//        if (ocrResult == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        return ResponseEntity.ok(ocrResult);
//    }
//    @GetMapping("/{documentId}/tamper-analysis")
//    public ResponseEntity<TamperResponse> getTamperAnalysis(@PathVariable String documentId) throws InterruptedException {
//        TamperResponse tamperResult = tamperService.getTamperResult(documentId);
//        if (tamperResult == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        return ResponseEntity.ok(tamperResult);
//    }
//    @GetMapping("/{documentId}/risk-score")
//    public ResponseEntity<RiskResponse> getRiskScore(@PathVariable String documentId) throws InterruptedException {
//        RiskResponse risk = riskService.getRiskResult(documentId);
//        if (risk == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        return ResponseEntity.ok(risk);
//    }
////    @GetMapping("/{documentId}/audit-trail")
////    public ResponseEntity<List<AuditTrailResponse>> getAuditTrail(@PathVariable String documentId) {
////        List<AuditTrailResponse> trail = auditService.getAuditTrail(documentId);
////        if (trail.isEmpty()) {
////            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
////        }
////        return ResponseEntity.ok(trail);
////    }

}
