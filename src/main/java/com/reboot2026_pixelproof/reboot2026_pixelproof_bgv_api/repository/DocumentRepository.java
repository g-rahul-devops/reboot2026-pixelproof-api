package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.repository;

import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.DocumentRecord;

import java.util.List;

public interface DocumentRepository {
    DocumentRecord findById(String id) throws InterruptedException;
    List<DocumentRecord> findAll() throws InterruptedException;
    void save(DocumentRecord documentRecord) throws InterruptedException;

    void updateSave(DocumentRecord documentRecord) throws InterruptedException;
    void updateOcrSave(DocumentRecord documentRecord) throws InterruptedException;
    void deleteById(String id) throws InterruptedException;
}
