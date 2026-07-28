package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.service;

import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.DocumentRecord;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.repository.DocumentRepository;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.util.HashUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FingerprintService {
    private final DocumentRepository repo;

    public FingerprintService(DocumentRepository repo) {
        this.repo = repo;
    }

    public String generateHash(MultipartFile file) {
        try {
            String hash = HashUtil.sha256(file.getBytes());
            DocumentRecord record = repo.findById(file.getOriginalFilename());
            record.setFile_hash(hash);
            record.setVerification_status("HASH_GENERATED");
            repo.save(record);
            return hash;
          } catch (IOException e) {
            throw new RuntimeException("Error generating hash", e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

