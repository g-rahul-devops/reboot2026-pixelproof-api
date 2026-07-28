package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class MetadataResponse {
    private String fileName;
    private long size;
    private String mimeType;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public MetadataResponse() {}

    public MetadataResponse(String fileName, long size, String mimeType,
                            LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.fileName = fileName;
        this.size = size;
        this.mimeType = mimeType;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    // getters and setters
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public long getSize() { return size; }
    public void setSize(long size) { this.size = size; }

    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getModifiedDate() { return modifiedDate; }
    public void setModifiedDate(LocalDateTime modifiedDate) { this.modifiedDate = modifiedDate; }
}

