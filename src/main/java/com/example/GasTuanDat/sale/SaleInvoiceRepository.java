package com.example.GasTuanDat.sale;

import com.example.GasTuanDat.report.dtos.SaleInvoiceReportDTO;
import com.example.GasTuanDat.sale.entities.SaleInvoiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SaleInvoiceRepository extends JpaRepository<SaleInvoiceEntity, UUID> {
    Page<SaleInvoiceEntity> findByGasBook_GasBookId(UUID gasBookId, Pageable pageable);

    Page<SaleInvoiceEntity> findByCustomer_CustomerId(UUID customerId, Pageable pageable);
    
    Page<SaleInvoiceEntity> findByCustomer_CustomerIdAndOrderType(UUID customerId, String orderType, Pageable pageable);

    boolean existsByInvoiceCodeIgnoreCase(String invoiceCode);

    @Query("""
            select si from SaleInvoiceEntity si
            left join si.customer c
            left join c.customerGroup cg
            left join si.stock s
            left join si.employee e
            where (:keyword is null or lower(si.invoiceCode) like lower(concat('%', cast(:keyword as string), '%')))
            and (cast(:startDate as timestamp) is null or si.invoiceDate >= :startDate)
            and (cast(:endDate as timestamp) is null or si.invoiceDate <= :endDate)
            and (cast(:customerId as uuid) is null or c.customerId = :customerId)
            and (cast(:stockId as uuid) is null or s.stockId = :stockId)
            and (cast(:employeeId as uuid) is null or e.employeeId = :employeeId)
            and (coalesce(:orderType, '') = '' or si.orderType = :orderType)
            and (cast(:customerGroupId as uuid) is null or cg.customerGroupId = :customerGroupId)
            order by si.invoiceDate desc""")
    Page<SaleInvoiceEntity> searchSaleInvoices(
            @org.springframework.data.repository.query.Param("keyword") String keyword,
            @org.springframework.data.repository.query.Param("startDate") java.time.OffsetDateTime startDate,
            @org.springframework.data.repository.query.Param("endDate") java.time.OffsetDateTime endDate,
            @org.springframework.data.repository.query.Param("customerId") UUID customerId,
            @org.springframework.data.repository.query.Param("stockId") UUID stockId,
            @org.springframework.data.repository.query.Param("employeeId") UUID employeeId,
            @org.springframework.data.repository.query.Param("orderType") String orderType,
            @org.springframework.data.repository.query.Param("customerGroupId") UUID customerGroupId,
            Pageable pageable);

    @Query(value = """
        SELECT
            TO_CHAR(s."invoiceDate", 'YYYY-MM-DD') as time,
            COUNT(s."invoiceId") as totalReceipts,
            COUNT(DISTINCT s."customerId") as totalCustomers,
            COALESCE(SUM(s."totalAmount"), 0) as totalAmount,
            COALESCE(SUM(s."discountAmount"), 0) as totalDiscount,
            COALESCE(SUM(s."totalAmount" - COALESCE(s."discountAmount", 0)), 0) as totalAfterDiscount,
            COALESCE(SUM(s."paidAmount"), 0) as totalRevenue,
            COALESCE(SUM(s."totalAmount" - COALESCE(s."discountAmount", 0) - COALESCE(s."paidAmount", 0)), 0) as totalDebt
        FROM "SaleInvoice" s
        WHERE s."invoiceDate" >= :startDate AND s."invoiceDate" <= :endDate AND CAST(s."orderType" AS text) = 'Xuathang'
        GROUP BY TO_CHAR(s."invoiceDate", 'YYYY-MM-DD')
        ORDER BY time DESC
    """, nativeQuery = true)
    List<SaleInvoiceReportDTO> getSaleInvoiceReport(@org.springframework.data.repository.query.Param("startDate") OffsetDateTime startDate, @org.springframework.data.repository.query.Param("endDate") OffsetDateTime endDate);

    @Query(value = """
        SELECT
            c."customerCode" as customerCode,
            c."fullName" as customerName,
            COALESCE(SUM(s."totalAmount"), 0) as totalAmount,
            COALESCE(SUM(s."discountAmount"), 0) as totalDiscount,
            COALESCE(SUM(s."totalAmount" - COALESCE(s."discountAmount", 0)), 0) as totalAfterDiscount,
            COALESCE(SUM(s."paidAmount"), 0) as totalPaid,
            COALESCE(SUM(s."totalAmount" - COALESCE(s."discountAmount", 0) - COALESCE(s."paidAmount", 0)), 0) as totalDebt
        FROM "SaleInvoice" s
        JOIN "Customer" c ON s."customerId" = c."customerId"
        WHERE s."invoiceDate" >= :startDate AND s."invoiceDate" <= :endDate AND CAST(s."orderType" AS text) = 'Xuathang'
        GROUP BY c."customerCode", c."fullName"
    """, nativeQuery = true)
    List<com.example.GasTuanDat.report.dtos.CustomerReportDTO> getCustomerReport(@org.springframework.data.repository.query.Param("startDate") OffsetDateTime startDate, @org.springframework.data.repository.query.Param("endDate") OffsetDateTime endDate);
}
