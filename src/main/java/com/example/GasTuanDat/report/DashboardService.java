package com.example.GasTuanDat.report;

import com.example.GasTuanDat.report.dtos.DashboardResponse;
import com.example.GasTuanDat.report.dtos.DashboardSummaryDTO;
import com.example.GasTuanDat.report.dtos.CustomerReportDTO;
import com.example.GasTuanDat.report.dtos.ProductExportReportDTO;
import com.example.GasTuanDat.report.dtos.SaleInvoiceReportDTO;
import com.example.GasTuanDat.sale.SaleInvoiceDetailRepository;
import com.example.GasTuanDat.sale.SaleInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final SaleInvoiceRepository saleInvoiceRepository;
    private final SaleInvoiceDetailRepository saleInvoiceDetailRepository;

    @Cacheable(value = "reports", key = "{'dashboard', #startDate, #endDate}")
    public DashboardResponse getDashboardData(OffsetDateTime startDate, OffsetDateTime endDate) {
        // 1. Summary
        DashboardSummaryDTO summaryDTO = saleInvoiceRepository.getDashboardSummary(startDate, endDate);
        Integer gasVolume = saleInvoiceDetailRepository.countTotalGasCylindersForDateRange(startDate, endDate);

        DashboardResponse.SummaryData summary = DashboardResponse.SummaryData.builder()
                .revenue(summaryDTO.getRevenue())
                .receipts(summaryDTO.getReceipts())
                .invoices(summaryDTO.getInvoices())
                .gasVolume(gasVolume)
                .build();

        // 2. Revenue Chart
        List<SaleInvoiceReportDTO> invoiceReports = saleInvoiceRepository.getSaleInvoiceReport(startDate, endDate);
        List<DashboardResponse.ChartData> chartData = invoiceReports.stream()
                .map(r -> {
                    String[] parts = r.getTime().split("-");
                    String name = parts.length == 3 ? parts[2] + "/" + parts[1] : r.getTime();
                    return DashboardResponse.ChartData.builder()
                            .name(name)
                            .value(r.getTotalAfterDiscount())
                            .build();
                })
                .sorted(Comparator.comparing(DashboardResponse.ChartData::getName)) // Sort chronologically if needed
                .collect(Collectors.toList());

        // 3. Top Products
        List<ProductExportReportDTO> exportReports = saleInvoiceDetailRepository.getExportReport(startDate, endDate);
        List<DashboardResponse.TopItemData> topProducts = exportReports.stream()
                .sorted(Comparator.comparing(ProductExportReportDTO::getTotalExportValue).reversed())
                .limit(10)
                .map(p -> {
                    double valueInMillions = p.getTotalExportValue().doubleValue() / 1000000.0;
                    return DashboardResponse.TopItemData.builder()
                            .name(p.getProductName())
                            .value(java.math.BigDecimal.valueOf(valueInMillions))
                            .display(String.format("%.1f tr", valueInMillions))
                            .build();
                })
                .collect(Collectors.toList());

        // 4. Top Customers
        List<CustomerReportDTO> customerReports = saleInvoiceRepository.getCustomerReport(startDate, endDate);
        List<DashboardResponse.TopItemData> topCustomers = customerReports.stream()
                .sorted(Comparator.comparing(CustomerReportDTO::getTotalAfterDiscount).reversed())
                .limit(10)
                .map(c -> {
                    double valueInMillions = c.getTotalAfterDiscount().doubleValue() / 1000000.0;
                    return DashboardResponse.TopItemData.builder()
                            .name(c.getCustomerName() != null ? c.getCustomerName() : "Khách lẻ")
                            .value(java.math.BigDecimal.valueOf(valueInMillions))
                            .display(String.format("%.1f tr", valueInMillions))
                            .build();
                })
                .collect(Collectors.toList());

        return DashboardResponse.builder()
                .summary(summary)
                .revenueChart(chartData)
                .topProducts(topProducts)
                .topCustomers(topCustomers)
                .build();
    }
}
