package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.repository;

import com.google.cloud.bigquery.*;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.OcrValidationResult;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class BigQueryOcrValidationResultRepository implements OcrValidationResultRepository {

    private final BigQuery bigQuery;
    private final String datasetName = "pixelproof";
    private final String tableName = "ocr_validation_results";

    public BigQueryOcrValidationResultRepository(BigQuery bigQuery) {
        this.bigQuery = bigQuery;
    }

    @Override
    public Optional<OcrValidationResult> findByDocumentId(String documentId) throws InterruptedException {
        String query = String.format("SELECT * FROM `%s.%s` WHERE documentId = '%s'", datasetName, tableName, documentId);
        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();

        for (FieldValueList row : bigQuery.query(queryConfig).iterateAll()) {
            return Optional.of(mapRowToOcrValidationResult(row));
        }
        return Optional.empty();
    }

    @Override
    public List<OcrValidationResult> findAll() throws InterruptedException {
        String query = String.format("SELECT * FROM `%s.%s`", datasetName, tableName);
        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();

        List<OcrValidationResult> results = new ArrayList<>();
        for (FieldValueList row : bigQuery.query(queryConfig).iterateAll()) {
            results.add(mapRowToOcrValidationResult(row));
        }
        return results;
    }

    @Override
    public void save(OcrValidationResult ocrValidationResult) throws InterruptedException {
        String query = String.format(
                "INSERT INTO `%s.%s` (id, documentId, validationStatus) VALUES ('%s', '%s', '%s')",
                datasetName, tableName,
                ocrValidationResult.getId(), ocrValidationResult.getDocumentId(),
                ocrValidationResult.getValidationStatus()
        );
        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();
        bigQuery.query(queryConfig);
    }

    @Override
    public void deleteById(String id) throws InterruptedException {
        String query = String.format("DELETE FROM `%s.%s` WHERE id = '%s'", datasetName, tableName, id);
        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();
        bigQuery.query(queryConfig);
    }

    private OcrValidationResult mapRowToOcrValidationResult(FieldValueList row) {
        OcrValidationResult result = new OcrValidationResult();
        result.setId(row.get("id").getStringValue());
        result.setDocumentId(row.get("documentId").getStringValue());
        result.setValidationStatus(row.get("validationStatus").getStringValue());
      //  result.setCreatedDate(row.get("createdDate").getTimestampValue());
        return result;
    }
}