package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.service;

import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.DocumentMetadata;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.DocumentRecord;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity.MetadataResponse;
import com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.repository.DocumentRepository;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

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
    public DocumentMetadata extractMetadata(MultipartFile file) {
        String mimeType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";
        String fileName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "unknown";
        LocalDateTime uploadTime = LocalDateTime.now();

        Integer pageCount = null;
        String author = null;
        String producer = null;
        String exifMetadata = null;
        boolean metadataValid = true;

        if (mimeType.contains("pdf")) {
            try (PDDocument document = Loader.loadPDF(file.getBytes())) {
                pageCount = document.getNumberOfPages();
                PDDocumentInformation info = document.getDocumentInformation();
                if (info != null) {
                    author = info.getAuthor();
                    producer = info.getProducer();
                }
            } catch (Exception e) {
                metadataValid = false;
            }
        }
        System.out.println("Extracted Metadata: fileName=" + fileName + ", mimeType=" + mimeType + ", pageCount=" + pageCount + ", author=" + author + ", producer=" + producer);
        return DocumentMetadata.builder()
                .documentId(UUID.randomUUID())
                .fileName(fileName)
                .fileSize(file.getSize())
                .mimeType(mimeType)
                .uploadTime(uploadTime)
                .pageCount(pageCount)
                .author(author)
                .producer(producer)
                .exifMetadata(exifMetadata)
                .metadataValid(metadataValid)
                .build();
    }
}


