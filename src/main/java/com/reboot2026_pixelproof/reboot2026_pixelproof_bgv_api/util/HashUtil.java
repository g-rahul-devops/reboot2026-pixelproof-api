package com.reboot2026_pixelproof.reboot2026_pixelproof_bgv_api.util;

import java.security.MessageDigest;
import java.util.Base64;

public class HashUtil {
    public static String sha256(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data);
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error generating SHA-256 hash", e);
        }
    }
}
