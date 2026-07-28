package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.repository;

import com.google.cloud.bigquery.*;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.TamperAnalysisResult;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class BigQueryTamperAnalysisResultRepository implements TamperAnalysisResultRepository {

    private final BigQuery bigQuery;
    private final String datasetName = "pixelproof";
    private final String tableName = "tamper_analysis_results";

    public BigQueryTamperAnalysisResultRepository(BigQuery bigQuery) {
        this.bigQuery = bigQuery;
    }

    @Override
    public Optional<TamperAnalysisResult> findByDocumentId(String documentId) throws InterruptedException {
        String query = String.format("SELECT * FROM `%s.%s` WHERE documentId = '%s'", datasetName, tableName, documentId);
        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();

        for (FieldValueList row : bigQuery.query(queryConfig).iterateAll()) {
            return Optional.of(mapRowToTamperAnalysisResult(row));
        }
        return Optional.empty();
    }

    @Override
    public List<TamperAnalysisResult> findAll() throws InterruptedException {
        String query = String.format("SELECT * FROM `%s.%s`", datasetName, tableName);
        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();

        List<TamperAnalysisResult> results = new ArrayList<>();
        for (FieldValueList row : bigQuery.query(queryConfig).iterateAll()) {
            results.add(mapRowToTamperAnalysisResult(row));
        }
        return results;
    }

    @Override
    public void save(TamperAnalysisResult tamperAnalysisResult) throws InterruptedException {
        String query = String.format(
                "INSERT INTO `%s.%s` (id, documentId, analysisResult, createdDate) VALUES ('%s', '%s', '%s')",
                datasetName, tableName,
                tamperAnalysisResult.getId(), tamperAnalysisResult.getDocumentId(),
                tamperAnalysisResult.getConfidenceScore() //tamperAnalysisResult.getCreatedDate()
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

    private TamperAnalysisResult mapRowToTamperAnalysisResult(FieldValueList row) {
        TamperAnalysisResult result = new TamperAnalysisResult();
        result.setId(row.get("id").getStringValue());
        result.setDocumentId(row.get("documentId").getStringValue());
        result.setConfidenceScore(row.get("confidenceScore").getNumericValue().intValue());
      //  result.setCreatedDate(row.get("createdDate").getTimestampValue());
        return result;
    }
}