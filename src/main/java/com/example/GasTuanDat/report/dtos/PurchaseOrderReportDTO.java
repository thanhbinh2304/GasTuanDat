package com.example.GasTuanDat.report.dtos;

import java.math.BigDecimal;

public interface PurchaseOrderReportDTO {
    String getTime();              // Ngày (yyyy-MM-dd)
    Long getTotalReceipts();       // Tổng số phiếu nhập hàng
    Long getTotalSuppliers();      // Tổng số nhà cung cấp
    BigDecimal getTotalAmount();           // Tổng tiền
    BigDecimal getTotalDiscount();         // Tổng giảm giá
    BigDecimal getTotalAfterDiscount();    // Tổng sau giảm giá
    BigDecimal getTotalPaid();             // Tổng trả
    BigDecimal getTotalDebt();             // Tổng còn nợ
}
