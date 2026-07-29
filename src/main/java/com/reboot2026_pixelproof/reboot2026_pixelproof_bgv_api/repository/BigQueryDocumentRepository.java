package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.repository;


import com.google.cloud.bigquery.*;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.DocumentRecord;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BigQueryDocumentRepository implements DocumentRepository {

    private final BigQuery bigQuery;
    private final String datasetName = "pixelproof";
    private final String tableName = "document_verification";

    public BigQueryDocumentRepository(BigQuery bigQuery) {
        this.bigQuery = bigQuery;
    }

    @Override
    public DocumentRecord findById(String id) throws InterruptedException {
        String query = String.format("SELECT * FROM `%s.%s` WHERE document_id = '%s'", datasetName, tableName, id);
        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();

        for (FieldValueList row : bigQuery.query(queryConfig).iterateAll()) {
            return mapRowToDocumentRecord(row);
        }
        return null;
    }

    @Override
    public List<DocumentRecord> findAll() throws InterruptedException {
        String query = String.format("SELECT * FROM `%s.%s`", datasetName, tableName);
        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();

        List<DocumentRecord> records = new ArrayList<>();
        for (FieldValueList row : bigQuery.query(queryConfig).iterateAll()) {
            records.add(mapRowToDocumentRecord(row));
        }
        return records;
    }
//    public String formatTimestamp(String timestamp) {
//        System.out.print("timestamp"+timestamp);
//        // Parse the input timestamp
//        if(!timestamp.equalsIgnoreCase(null)) {
//            LocalDateTime dateTime = LocalDateTime.parse(timestamp);
//            // Format it to BigQuery-compatible TIMESTAMP format
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
//            return dateTime.format(formatter);
//        }
//        return null;
//    }

    @Override
    public void save(DocumentRecord documentRecord) throws InterruptedException {
        String query = String.format(
                "INSERT INTO `%s.%s` (document_id, verification_status, gcs_path, file_name,employee_id) " +
                        "VALUES ('%s', '%s', '%s', '%s','%s')",
                datasetName, tableName,
                documentRecord.getDocument_id(),
                documentRecord.getVerification_status(),
                documentRecord.getGcs_path(),
                documentRecord.getFile_name(),
                documentRecord.getEmployee_id()
        );

        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();
        bigQuery.query(queryConfig);
    }

    @Override
    public void updateSave(DocumentRecord documentRecord) throws InterruptedException {
        String query = String.format(
                "UPDATE `%s.%s` " +
                        "SET file_hash = '%s', verification_status = '%s' " +
                        "WHERE document_id = '%s'",
                datasetName, tableName,
                documentRecord.getFile_hash(),
                documentRecord.getVerification_status(),
                documentRecord.getDocument_id()
        );

        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();
        bigQuery.query(queryConfig);
    }
    @Override
    public void updateOcrSave(DocumentRecord documentRecord) throws InterruptedException {
        String query = String.format(
                "UPDATE `%s.%s` " +
                        "SET score = %d, " + // Changed '%s' to %d for numeric value
                        "decision = '%s', " +
                        "reasonCodes = '%s' " +
                        "WHERE document_id = '%s'",
                datasetName, tableName,
                documentRecord.getScore(), // Ensure this is an integer
                documentRecord.getDecision(),
                documentRecord.getReasonCodes(),
                documentRecord.getDocument_id()
        );

        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();
        bigQuery.query(queryConfig);
    }


    private DocumentRecord mapRowToDocumentRecord(FieldValueList row) {
        DocumentRecord record = new DocumentRecord();

        record.setDocument_id(row.get("document_id").isNull() ? null : row.get("document_id").getStringValue());
        record.setEmployee_id(row.get("employee_id").isNull() ? null : row.get("employee_id").getStringValue());
        record.setScore(row.get("score").isNull() ? 0 : row.get("score").getNumericValue().intValue());
        record.setFile_name(row.get("file_name").isNull() ? null : row.get("file_name").getStringValue());
        record.setGcs_path(row.get("gcs_path").isNull() ? null : row.get("gcs_path").getStringValue());
        record.setVerification_status(row.get("verification_status").isNull() ? null : row.get("verification_status").getStringValue());
        record.setDocument_id(row.get("decision").isNull() ? null : row.get("decision").getStringValue());
        record.setEmployee_id(row.get("reasonCodes").isNull() ? null : row.get("reasonCodes").getStringValue());

        return record;

    }
}
