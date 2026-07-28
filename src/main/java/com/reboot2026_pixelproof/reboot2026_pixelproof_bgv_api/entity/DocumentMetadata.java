package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class DocumentMetadata {

    private UUID documentId;

    private String fileName;

    private Long fileSize;

    private String mimeType;

    private LocalDateTime uploadTime;

    private Integer pageCount;

    private String author;

    private String producer;

    private String exifMetadata;

    private boolean metadataValid;

    private LocalDateTime createdAt;

}
