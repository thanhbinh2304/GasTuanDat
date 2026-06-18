package com.example.GasTuanDat.report.dtos;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class DashboardResponse {
    private SummaryData summary;
    private List<ChartData> revenueChart;
    private List<TopItemData> topProducts;
    private List<TopItemData> topCustomers;

    @Data
    @Builder
    public static class SummaryData {
        private BigDecimal revenue;
        private BigDecimal receipts;
        private Long invoices;
        private Integer gasVolume;
    }

    @Data
    @Builder
    public static class ChartData {
        private String name;
        private BigDecimal value;
    }

    @Data
    @Builder
    public static class TopItemData {
        private String name;
        private BigDecimal value;
        private String display;
    }
}
