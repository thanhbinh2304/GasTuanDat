package com.example.GasTuanDat.purchase;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.GasTuanDat.purchase.entities.PurchaseOrderEntity;
import com.example.GasTuanDat.report.dtos.PurchaseOrderReportDTO;

import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrderEntity, UUID> {
    boolean existsByPurchaseCodeIgnoreCase(String purchaseCode);

    @Query("""
            select po from PurchaseOrderEntity po
            left join po.supplier sup
            left join po.stock st
            left join po.employee emp
            where (:keyword is null or lower(po.purchaseCode) like lower(concat('%', cast(:keyword as string), '%')))
            and (cast(:startDate as timestamp) is null or po.purchaseDate >= :startDate)
            and (cast(:endDate as timestamp) is null or po.purchaseDate <= :endDate)
            and (cast(:supplierId as uuid) is null or sup.supplierId = :supplierId)
            and (cast(:stockId as uuid) is null or st.stockId = :stockId)
            and (cast(:employeeId as uuid) is null or emp.employeeId = :employeeId)
            and (coalesce(:orderType, '') = '' or po.orderType = :orderType)
            order by po.purchaseDate desc""")
    Page<PurchaseOrderEntity> searchPurchaseOrders(
            @Param("keyword") String keyword,
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate,
            @Param("supplierId") UUID supplierId,
            @Param("stockId") UUID stockId,
            @Param("employeeId") UUID employeeId,
            @Param("orderType") String orderType,
            Pageable pageable);

    @Query("SELECT CAST(po.purchaseDate AS date) AS time, " +
           "COUNT(po) AS totalReceipts, " +
           "COUNT(DISTINCT po.supplier.supplierId) AS totalSuppliers, " +
           "COALESCE(SUM(po.totalAmount), 0) AS totalAmount, " +
           "COALESCE(SUM(po.discountAmount), 0) AS totalDiscount, " +
           "COALESCE(SUM(po.totalAmount - COALESCE(po.discountAmount, 0)), 0) AS totalAfterDiscount, " +
           "COALESCE(SUM(po.paidAmount), 0) AS totalPaid, " +
           "COALESCE(SUM(po.totalAmount - COALESCE(po.discountAmount, 0) - COALESCE(po.paidAmount, 0)), 0) AS totalDebt " +
           "FROM PurchaseOrderEntity po " +
           "WHERE po.purchaseDate >= :startDate AND po.purchaseDate <= :endDate AND CAST(po.orderType AS string) = 'Nhaphang' " +
           "GROUP BY CAST(po.purchaseDate AS date) " +
           "ORDER BY CAST(po.purchaseDate AS date) DESC")
    List<PurchaseOrderReportDTO> getPurchaseOrderReport(@Param("startDate") OffsetDateTime startDate, @Param("endDate") OffsetDateTime endDate);

    @Query("SELECT CAST(po.supplier.supplierId AS string) AS supplierCode, " +
           "po.supplier.fullName AS supplierName, " +
           "COALESCE(SUM(po.totalAmount), 0) AS totalAmount, " +
           "COALESCE(SUM(po.discountAmount), 0) AS totalDiscount, " +
           "COALESCE(SUM(po.totalAmount - COALESCE(po.discountAmount, 0)), 0) AS totalAfterDiscount, " +
           "COALESCE(SUM(po.paidAmount), 0) AS totalPaid, " +
           "COALESCE(SUM(po.totalAmount - COALESCE(po.discountAmount, 0) - COALESCE(po.paidAmount, 0)), 0) AS totalDebt " +
           "FROM PurchaseOrderEntity po " +
           "WHERE po.purchaseDate >= :startDate AND po.purchaseDate <= :endDate AND po.supplier IS NOT NULL AND CAST(po.orderType AS string) = 'Nhaphang' " +
           "GROUP BY po.supplier.supplierId, po.supplier.fullName " +
           "ORDER BY po.supplier.fullName ASC")
    List<com.example.GasTuanDat.report.dtos.SupplierReportDTO> getSupplierReport(@Param("startDate") OffsetDateTime startDate, @Param("endDate") OffsetDateTime endDate);
}
