package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.repository;

import com.google.cloud.bigquery.*;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.RiskScoreResult;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class BigQueryRiskScoreResultRepository implements RiskScoreResultRepository {

    private final BigQuery bigQuery;
    private final String datasetName = "pixelproof";
    private final String tableName = "risk_score_results";

    public BigQueryRiskScoreResultRepository(BigQuery bigQuery) {
        this.bigQuery = bigQuery;
    }

    @Override
    public Optional<RiskScoreResult> findByDocumentId(String documentId) throws InterruptedException {
        String query = String.format("SELECT * FROM `%s.%s` WHERE documentId = '%s'", datasetName, tableName, documentId);
        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();

        for (FieldValueList row : bigQuery.query(queryConfig).iterateAll()) {
            return Optional.of(mapRowToRiskScoreResult(row));
        }
        return Optional.empty();
    }

    @Override
    public List<RiskScoreResult> findAll() throws InterruptedException {
        String query = String.format("SELECT * FROM `%s.%s`", datasetName, tableName);
        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();

        List<RiskScoreResult> results = new ArrayList<>();
        for (FieldValueList row : bigQuery.query(queryConfig).iterateAll()) {
            results.add(mapRowToRiskScoreResult(row));
        }
        return results;
    }

    @Override
    public void save(RiskScoreResult riskScoreResult) throws InterruptedException {
        String query = String.format(
                "INSERT INTO `%s.%s` (id, documentId, riskScore, createdDate) VALUES ('%s', '%s', %f)",
                datasetName, tableName,
                riskScoreResult.getId(), riskScoreResult.getDocumentId(),
                riskScoreResult.getScore()
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

    private RiskScoreResult mapRowToRiskScoreResult(FieldValueList row) {
        RiskScoreResult result = new RiskScoreResult();
        result.setId(row.get("id").getStringValue());
        result.setDocumentId(row.get("documentId").getStringValue());
        result.setScore(row.get("score").getNumericValue().intValue());
    //    result.setCreatedDate(row.get("createdDate").getTimestampValue());
        return result;
    }
}