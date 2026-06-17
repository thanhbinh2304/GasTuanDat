package com.example.GasTuanDat.sale;

import com.example.GasTuanDat.sale.entities.SaleInvoiceDetailEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.GasTuanDat.report.dtos.ProductExportReportDTO;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SaleInvoiceDetailRepository extends JpaRepository<SaleInvoiceDetailEntity, UUID> {
    Page<SaleInvoiceDetailEntity> findByInvoice_GasBook_GasBookId(UUID gasBookId, Pageable pageable);
    
    java.util.List<SaleInvoiceDetailEntity> findByInvoice_InvoiceId(UUID invoiceId);
    
    void deleteByInvoice_InvoiceId(UUID invoiceId);

    @Query(value = """
        SELECT
            p."productCode" as productCode,
            (p."productName" || COALESCE((SELECT ' - ' || STRING_AGG(pa."attributeValue", ' - ') FROM "ProductAttribute" pa WHERE pa."productId" = p."productId"), '')) as productName,
            p."unit" as unit,
            COALESCE(SUM(d."quantity"), 0) as qty,
            COALESCE(SUM(d."total"), 0) as totalExportValue,
            COALESCE(AVG(p."cost"), 0) as costPrice,
            COALESCE(SUM(d."quantity" * p."cost"), 0) as totalCost,
            COALESCE(SUM(d."total" - (d."quantity" * p."cost")), 0) as profit
        FROM "InvoiceDetail" d
        JOIN "SaleInvoice" s ON d."invoiceId" = s."invoiceId"
        JOIN "Product" p ON d."productId" = p."productId"
        WHERE s."invoiceDate" >= :startDate AND s."invoiceDate" <= :endDate AND CAST(s."orderType" AS text) = 'Xuathang'
        GROUP BY p."productId", p."productCode", p."productName", p."unit"
    """, nativeQuery = true)
    List<ProductExportReportDTO> getExportReport(@Param("startDate") OffsetDateTime startDate, @Param("endDate") OffsetDateTime endDate);

    @Query(value = """
        SELECT COALESCE(SUM(d."quantity"), 0)
        FROM "InvoiceDetail" d
        JOIN "SaleInvoice" s ON d."invoiceId" = s."invoiceId"
        JOIN "Product" p ON d."productId" = p."productId"
        JOIN "ProductCategory" c ON p."categoryId" = c."categoryId"
        WHERE s."gasBookId" = :gasBookId 
          AND CAST(s."orderType" AS text) = 'Xuathang'
          AND LOWER(c."categoryName") LIKE '%bình gas%'
    """, nativeQuery = true)
    Integer countTotalGasCylindersForGasBook(@Param("gasBookId") UUID gasBookId);
}
