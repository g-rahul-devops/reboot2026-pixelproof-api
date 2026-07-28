package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.entity;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentRecord {

    private String document_id;

    private String gcs_path;
    private String file_hash;
    private LocalDateTime uploaded_at;
    private String verification_status;

    private String file_name;
    private long size;
    private String mimeType;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

}

