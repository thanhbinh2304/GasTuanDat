package com.example.GasTuanDat.report;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.GasTuanDat.report.dtos.GasBookReportDTO;
import com.example.GasTuanDat.report.dtos.ProductExportReportDTO;
import com.example.GasTuanDat.report.dtos.ProductImportReportDTO;
import com.example.GasTuanDat.report.dtos.PurchaseOrderReportDTO;
import com.example.GasTuanDat.report.dtos.SaleInvoiceReportDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/products/import")
    public ResponseEntity<List<ProductImportReportDTO>> getImportReport(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        OffsetDateTime start = startDate.atStartOfDay().atOffset(ZoneOffset.ofHours(7));
        OffsetDateTime end = endDate.atTime(23, 59, 59).atOffset(ZoneOffset.ofHours(7));
        
        return ResponseEntity.ok(reportService.getImportReport(start, end));
    }

    @GetMapping("/products/export")
    public ResponseEntity<List<ProductExportReportDTO>> getExportReport(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        OffsetDateTime start = startDate.atStartOfDay().atOffset(ZoneOffset.ofHours(7));
        OffsetDateTime end = endDate.atTime(23, 59, 59).atOffset(ZoneOffset.ofHours(7));
        
        return ResponseEntity.ok(reportService.getExportReport(start, end));
    }

    @GetMapping("/invoices/export-summary")
    public ResponseEntity<List<SaleInvoiceReportDTO>> getExportSummaryReport(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        OffsetDateTime start = startDate.atStartOfDay().atOffset(ZoneOffset.ofHours(7));
        OffsetDateTime end = endDate.atTime(23, 59, 59).atOffset(ZoneOffset.ofHours(7));

        return ResponseEntity.ok(reportService.getExportSummaryReport(start, end));
    }

    @GetMapping("/invoices/import-summary")
    public ResponseEntity<List<PurchaseOrderReportDTO>> getImportSummaryReport(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        OffsetDateTime start = startDate.atStartOfDay().atOffset(ZoneOffset.ofHours(7));
        OffsetDateTime end = endDate.atTime(23, 59, 59).atOffset(ZoneOffset.ofHours(7));

        return ResponseEntity.ok(reportService.getImportSummaryReport(start, end));
    }

    @GetMapping("/gas-books/summary")
    public ResponseEntity<List<GasBookReportDTO>> getGasBookSummaryReport(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        OffsetDateTime start = startDate.atStartOfDay().atOffset(ZoneOffset.ofHours(7));
        OffsetDateTime end = endDate.atTime(23, 59, 59).atOffset(ZoneOffset.ofHours(7));

        return ResponseEntity.ok(reportService.getGasBookSummaryReport(start, end));
    }

    @GetMapping("/customers/summary")
    public ResponseEntity<List<com.example.GasTuanDat.report.dtos.CustomerReportDTO>> getCustomerSummaryReport(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        OffsetDateTime start = startDate.atStartOfDay().atOffset(ZoneOffset.ofHours(7));
        OffsetDateTime end = endDate.atTime(23, 59, 59).atOffset(ZoneOffset.ofHours(7));

        return ResponseEntity.ok(reportService.getCustomerSummaryReport(start, end));
    }

    @GetMapping("/suppliers/summary")
    public ResponseEntity<List<com.example.GasTuanDat.report.dtos.SupplierReportDTO>> getSupplierSummaryReport(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        OffsetDateTime start = startDate.atStartOfDay().atOffset(ZoneOffset.ofHours(7));
        OffsetDateTime end = endDate.atTime(23, 59, 59).atOffset(ZoneOffset.ofHours(7));

        return ResponseEntity.ok(reportService.getSupplierSummaryReport(start, end));
    }
}
