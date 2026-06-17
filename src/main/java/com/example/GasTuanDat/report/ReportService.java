package com.example.GasTuanDat.report;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.GasTuanDat.purchase.PurchaseDetailRepository;
import com.example.GasTuanDat.purchase.PurchaseOrderRepository;
import com.example.GasTuanDat.report.dtos.GasBookReportDTO;
import com.example.GasTuanDat.report.dtos.ProductExportReportDTO;
import com.example.GasTuanDat.report.dtos.ProductImportReportDTO;
import com.example.GasTuanDat.report.dtos.PurchaseOrderReportDTO;
import com.example.GasTuanDat.report.dtos.SaleInvoiceReportDTO;
import com.example.GasTuanDat.gasBook.GasBookRepository;
import com.example.GasTuanDat.sale.SaleInvoiceDetailRepository;
import com.example.GasTuanDat.sale.SaleInvoiceRepository;

import org.springframework.cache.annotation.Cacheable;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final PurchaseDetailRepository purchaseDetailRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SaleInvoiceDetailRepository saleInvoiceDetailRepository;
    private final SaleInvoiceRepository saleInvoiceRepository;
    private final GasBookRepository gasBookRepository;

    @Cacheable(value = "reports", key = "{#root.methodName, #startDate, #endDate}")
    public List<ProductImportReportDTO> getImportReport(OffsetDateTime startDate, OffsetDateTime endDate) {
        return purchaseDetailRepository.getImportReport(startDate, endDate);
    }

    @Cacheable(value = "reports", key = "{#root.methodName, #startDate, #endDate}")
    public List<ProductExportReportDTO> getExportReport(OffsetDateTime startDate, OffsetDateTime endDate) {
        return saleInvoiceDetailRepository.getExportReport(startDate, endDate);
    }

    @Cacheable(value = "reports", key = "{#root.methodName, #startDate, #endDate}")
    public List<SaleInvoiceReportDTO> getExportSummaryReport(OffsetDateTime startDate, OffsetDateTime endDate) {
        return saleInvoiceRepository.getSaleInvoiceReport(startDate, endDate);
    }

    @Cacheable(value = "reports", key = "{#root.methodName, #startDate, #endDate}")
    public List<PurchaseOrderReportDTO> getImportSummaryReport(OffsetDateTime startDate, OffsetDateTime endDate) {
        return purchaseOrderRepository.getPurchaseOrderReport(startDate, endDate);
    }

    @Cacheable(value = "reports", key = "{#root.methodName, #startDate, #endDate}")
    public List<GasBookReportDTO> getGasBookSummaryReport(OffsetDateTime startDate, OffsetDateTime endDate) {
        return gasBookRepository.getGasBookReport(startDate, endDate);
    }

    @Cacheable(value = "reports", key = "{#root.methodName, #startDate, #endDate}")
    public List<com.example.GasTuanDat.report.dtos.CustomerReportDTO> getCustomerSummaryReport(OffsetDateTime startDate, OffsetDateTime endDate) {
        return saleInvoiceRepository.getCustomerReport(startDate, endDate);
    }

    @Cacheable(value = "reports", key = "{#root.methodName, #startDate, #endDate}")
    public List<com.example.GasTuanDat.report.dtos.SupplierReportDTO> getSupplierSummaryReport(OffsetDateTime startDate, OffsetDateTime endDate) {
        return purchaseOrderRepository.getSupplierReport(startDate, endDate);
    }
}
