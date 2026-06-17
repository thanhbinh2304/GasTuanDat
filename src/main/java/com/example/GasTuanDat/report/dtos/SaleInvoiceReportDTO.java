package com.example.GasTuanDat.report.dtos;

import java.math.BigDecimal;

public interface SaleInvoiceReportDTO {
    String getTime();           // Ngày (yyyy-MM-dd)
    Long getTotalReceipts();    // Tổng số phiếu xuất hàng
    Long getTotalCustomers();   // Tổng số khách hàng
    BigDecimal getTotalAmount();         // Tổng tiền
    BigDecimal getTotalDiscount();       // Tổng giảm giá
    BigDecimal getTotalAfterDiscount();  // Tổng sau giảm giá (= totalAmount - totalDiscount)
    BigDecimal getTotalRevenue();        // Tổng thu (paidAmount)
    BigDecimal getTotalDebt();           // Tổng còn nợ
}
