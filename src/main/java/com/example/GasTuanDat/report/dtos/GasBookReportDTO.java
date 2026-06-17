package com.example.GasTuanDat.report.dtos;

import java.math.BigDecimal;

public interface GasBookReportDTO {
    String getGasBookCode();       // Mã sổ gas (gasBookId dạng string hoặc code)
    String getCustomerName();      // Tên khách hàng
    BigDecimal getTotalAmount();           // Tổng tiền
    BigDecimal getTotalDiscount();         // Tổng giảm giá
    BigDecimal getTotalAfterDiscount();    // Tổng sau giảm giá
    BigDecimal getTotalPaid();             // Tổng thu
    BigDecimal getTotalDebt();             // Tổng còn nợ
}
