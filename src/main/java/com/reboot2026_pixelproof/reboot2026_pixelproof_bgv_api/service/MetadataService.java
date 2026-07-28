package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.service;

import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.DocumentRecord;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.MetadataResponse;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.repository.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class MetadataService {
    private final DocumentRepository repo;

    public MetadataService(DocumentRepository repo) {
        this.repo = repo;
    }

    public MetadataResponse getMetadata(String documentId) throws InterruptedException {
        DocumentRecord record= repo.findById(documentId);
               return new MetadataResponse(
                        record.getFile_name(),
                        record.getSize(),
                        record.getMimeType(),
                        record.getCreatedDate(),
                        record.getModifiedDate()
                );
    }

    // For upload flow: extract metadata from file and persist
    public MetadataResponse extractMetadata(MultipartFile file) {
        MetadataResponse response = new MetadataResponse();
        response.setFileName(file.getOriginalFilename());
        response.setSize(file.getSize());
        response.setMimeType(file.getContentType());
        response.setCreatedDate(LocalDateTime.now());   // stub
        response.setModifiedDate(LocalDateTime.now());  // stub
        return response;
    }
}


