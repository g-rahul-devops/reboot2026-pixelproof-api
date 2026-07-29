package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.DocumentRecord;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class StorageService {
    private final DocumentRepository repo;

    private final Storage gcsStorage;
    private final String bucketName;

    public StorageService(DocumentRepository repo,
                          Storage gcsStorage,
                          @Value("${gcs.bucket-name}") String bucketName) {
        this.repo = repo;
        this.gcsStorage = gcsStorage; // Injected Storage bean
        this.bucketName = bucketName;
    }

    public String store(MultipartFile file, String employee_id) {
        String docId = UUID.randomUUID().toString();
        String gcsPath = docId + "/" + file.getOriginalFilename();
        try {
            BlobId blobId = BlobId.of(bucketName, gcsPath);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
            gcsStorage.create(blobInfo, file.getBytes());

            saveDocumentRecord(file, docId, gcsPath, employee_id);


            return docId;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file in GCS", e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveDocumentRecord(MultipartFile file,String docId,String gcsPath, String employee_id) throws InterruptedException {
        DocumentRecord record = new DocumentRecord();
        record.setDocument_id(docId);
        record.setEmployee_id(employee_id);
        record.setGcs_path(gcsPath);
        System.out.println("Original filename " +file.getOriginalFilename());
        record.setFile_name(file.getOriginalFilename());
        record.setUploaded_at(LocalDateTime.now());
        record.setVerification_status("STORED_IN_GCS");
        repo.save(record);
    }
    public void saveAuditLedger(int EmpId,String empName,String docName,String status){

    }
    public String getStatus(String documentId) throws InterruptedException {
        return repo.findById(documentId).getVerification_status();
    }
}

