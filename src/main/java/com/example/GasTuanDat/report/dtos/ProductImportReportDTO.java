package com.example.GasTuanDat.report.dtos;

import java.math.BigDecimal;

public interface ProductImportReportDTO {
    String getProductCode();
    String getProductName();
    Short getUnit();
    Long getQty();
    BigDecimal getTotalImportValue();
}
