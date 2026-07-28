//package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.service;
//
//import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.AuditTrailEvent;
//import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.AuditTrailResponse;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class AuditService {
//
//    private final LedgerClient ledgerClient;
//    private final String projectId;
//    private final String ledgerId;
//
//    public AuditService(@Value("${gcp.project-id}") String projectId,
//                        @Value("${gcp.ledger-id}") String ledgerId) throws Exception {
//        this.projectId = projectId;
//        this.ledgerId = ledgerId;
//        this.ledgerClient = LedgerClient.create();
//    }
//
//    public void logStage(String docId, String stage, String status, String fingerprint) {
//        AuditTrailEvent event = new AuditTrailEvent();
//        event.setDocumentReferenceId(docId);
//        event.setStageName(stage);
//        event.setStatus(status);
//        event.setTimestamp(LocalDateTime.now());
//        event.setSha256Fingerprint(fingerprint);
//        event.setResultSummary("Stage " + stage + " completed");
//        event.setRiskIndicators("N/A");
//
//        LedgerEntry entry = LedgerEntry.newBuilder()
//                .putFields("documentReferenceId", Value.newBuilder().setStringValue(docId).build())
//                .putFields("stageName", Value.newBuilder().setStringValue(stage).build())
//                .putFields("status", Value.newBuilder().setStringValue(status).build())
//                .putFields("sha256Fingerprint", Value.newBuilder().setStringValue(fingerprint).build())
//                .putFields("resultSummary", Value.newBuilder().setStringValue(event.getResultSummary()).build())
//                .putFields("riskIndicators", Value.newBuilder().setStringValue(event.getRiskIndicators()).build())
//                .setTimestamp(Timestamp.newBuilder()
//                        .setSeconds(Instant.now().getEpochSecond())
//                        .build())
//                .build();
//
//        AppendLedgerEntryRequest request = AppendLedgerEntryRequest.newBuilder()
//                .setParent(LedgerName.of(projectId, ledgerId).toString())
//                .setLedgerEntry(entry)
//                .build();
//
//        ledgerClient.appendLedgerEntry(request);
//    }
//
//    public List<AuditTrailResponse> getAuditTrail(String documentId) {
//        ListLedgerEntriesRequest request = ListLedgerEntriesRequest.newBuilder()
//                .setParent(LedgerName.of(projectId, ledgerId).toString())
//                .setFilter("documentReferenceId=\"" + documentId + "\"")
//                .build();
//
//        List<AuditTrailResponse> responses = new ArrayList<>();
//        for (LedgerEntry entry : ledgerClient.listLedgerEntries(request).iterateAll()) {
//            AuditTrailResponse resp = new AuditTrailResponse(
//                    entry.getFieldsOrThrow("documentReferenceId").getStringValue(),
//                    entry.getFieldsOrThrow("stageName").getStringValue(),
//                    entry.getFieldsOrThrow("status").getStringValue(),
//
//
//                    LocalDateTime.ofInstant(
//                            Instant.ofEpochSecond(entry.getTimestamp().getSeconds()),
//                            ZoneId.systemDefault()),
//                    entry.getFieldsOrThrow("sha256Fingerprint").getStringValue(),
//                    entry.getFieldsOrThrow("resultSummary").getStringValue(),
//                    entry.getFieldsOrThrow("riskIndicators").getStringValue(),
//                    entry.containsFields("previousAuditHash")
//                            ? entry.getFieldsOrThrow("previousAuditHash").getStringValue()
//                            : null
//            );
//            responses.add(resp);
//        }
//        return responses;
//    }
//}
//
