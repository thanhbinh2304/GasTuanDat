package com.example.GasTuanDat.common.constants;

import java.text.Normalizer;
import java.util.Locale;

public final class RoleConstants {
    public static final String ADMIN = "ADMIN";
    public static final String SALES = "NHAN_VIEN_SALES";
    public static final String WAREHOUSE = "NHAN_VIEN_KHO";

    private RoleConstants() {
        // Utility class
    }

    public static String canonicalize(String roleName) {
        if (roleName == null || roleName.isBlank()) {
            return null;
        }

        String normalized = Normalizer.normalize(roleName, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toUpperCase(Locale.ROOT)
                .replaceAll("[^A-Z0-9]+", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_|_$", "");

        if (ADMIN.equals(normalized) || normalized.contains("ADMIN")) {
            return ADMIN;
        }
        if (SALES.equals(normalized) || normalized.contains("SALE") || normalized.contains("NHAN_VIEN")) {
            return SALES;
        }
        if (WAREHOUSE.equals(normalized) || normalized.contains("KHO") || normalized.contains("WAREHOUSE")) {
            return WAREHOUSE;
        }

        return null;
    }
}
