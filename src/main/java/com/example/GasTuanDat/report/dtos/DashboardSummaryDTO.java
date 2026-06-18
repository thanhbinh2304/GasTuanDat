package com.example.GasTuanDat.report.dtos;

import java.math.BigDecimal;

public interface DashboardSummaryDTO {
    Long getInvoices();
    BigDecimal getRevenue();
    BigDecimal getReceipts();
}
