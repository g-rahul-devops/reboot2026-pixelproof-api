package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class BigQueryConfig {
    @Bean
    public Storage createStorage() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
        // Create the Storage object with the project ID
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(credentials)
                .setProjectId("ltc-hack2026-team27")
                .build()
                .getService();

        // Access the bucket
        String bucketName = "pixelproof-dev";
        Bucket bucket = storage.get(bucketName);

        if (bucket == null) {
            System.out.println("Bucket not found: " + bucketName);
        } else {
            System.out.println("Bucket accessed successfully: " + bucket.getName());
        }

        return storage;
    }

    @Bean
    public BigQuery bigQuery() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
        BigQuery bigQuery = BigQueryOptions.newBuilder()
                .setCredentials(credentials)
                .setProjectId("ltc-hack2026-team27")
                .build()
                .getService();
        return bigQuery;

    }
}
