package com.example.GasTuanDat.report.dtos;

public interface SupplierReportDTO {
    String getSupplierCode();
    String getSupplierName();
    java.math.BigDecimal getTotalAmount();
    java.math.BigDecimal getTotalDiscount();
    java.math.BigDecimal getTotalAfterDiscount();
    java.math.BigDecimal getTotalPaid();
    java.math.BigDecimal getTotalDebt();
}
