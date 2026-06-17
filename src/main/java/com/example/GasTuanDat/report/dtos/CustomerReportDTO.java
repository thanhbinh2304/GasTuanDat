package com.example.GasTuanDat.report.dtos;

public interface CustomerReportDTO {
    String getCustomerCode();
    String getCustomerName();
    java.math.BigDecimal getTotalAmount();
    java.math.BigDecimal getTotalDiscount();
    java.math.BigDecimal getTotalAfterDiscount();
    java.math.BigDecimal getTotalPaid();
    java.math.BigDecimal getTotalDebt();
}
