package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "vertex-ai")
@Getter
@Setter
public class VertexAiProperties {

    private boolean enabled;
    private String projectId;
    private String location;
    private String model;
}
