package com.example.GasTuanDat.purchase;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.GasTuanDat.purchase.entities.PurchaseDetailEntity;
import com.example.GasTuanDat.report.dtos.ProductImportReportDTO;

import java.time.OffsetDateTime;

import java.util.List;

@Repository
public interface PurchaseDetailRepository extends JpaRepository<PurchaseDetailEntity, UUID> {
    List<PurchaseDetailEntity> findByPurchasePurchaseId(UUID purchaseId);
    void deleteByPurchase_PurchaseId(UUID purchaseId);

    @Query(value = """
        SELECT
            p."productCode" as productCode,
            (p."productName" || COALESCE((SELECT ' - ' || STRING_AGG(pa."attributeValue", ' - ') FROM "ProductAttribute" pa WHERE pa."productId" = p."productId"), '')) as productName,
            p."unit" as unit,
            COALESCE(SUM(d."quantity"), 0) as qty,
            COALESCE(SUM(d."total"), 0) as totalImportValue
        FROM "PurchaseDetail" d
        JOIN "PurchaseOrder" o ON d."purchaseId" = o."purchaseId"
        JOIN "Product" p ON d."productId" = p."productId"
        WHERE o."purchaseDate" >= :startDate AND o."purchaseDate" <= :endDate
        GROUP BY p."productId", p."productCode", p."productName", p."unit"
    """, nativeQuery = true)
    List<ProductImportReportDTO> getImportReport(@Param("startDate") OffsetDateTime startDate, @Param("endDate") OffsetDateTime endDate);
}
