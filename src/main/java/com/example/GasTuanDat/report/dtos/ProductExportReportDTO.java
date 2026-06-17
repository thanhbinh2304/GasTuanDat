package com.example.GasTuanDat.report.dtos;

import java.math.BigDecimal;

public interface ProductExportReportDTO {
    String getProductCode();
    String getProductName();
    Short getUnit();
    Long getQty();
    BigDecimal getTotalExportValue();
    BigDecimal getCostPrice();
    BigDecimal getTotalCost();
    BigDecimal getProfit();
}
