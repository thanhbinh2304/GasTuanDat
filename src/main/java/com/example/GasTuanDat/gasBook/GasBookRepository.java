package com.example.GasTuanDat.gasBook;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.GasTuanDat.gasBook.entities.GasBookEntity;
import com.example.GasTuanDat.report.dtos.GasBookReportDTO;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface GasBookRepository extends JpaRepository<GasBookEntity, UUID> {

    boolean existsByGasBookCodeIgnoreCase(String gasBookCode);

    java.util.Optional<GasBookEntity> findByGasBookCodeIgnoreCase(String gasBookCode);

    @Query("SELECT gb.gasBookCode FROM GasBookEntity gb WHERE gb.gasBookCode LIKE concat(:prefix, '%')")
    java.util.List<String> findGasBookCodesByPrefix(@Param("prefix") String prefix);

    @Query("""
            SELECT gb FROM GasBookEntity gb
            LEFT JOIN gb.customerGroup cg
            WHERE (:keyword IS NULL OR (
                LOWER(gb.gasBookCode) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')) OR
                LOWER(gb.fullName) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')) OR
                gb.phoneNumber LIKE CONCAT('%', CAST(:keyword AS string), '%')
            ))
              AND (:customerCode IS NULL OR LOWER(gb.gasBookCode) LIKE LOWER(CONCAT('%', CAST(:customerCode AS string), '%')))
              AND (:fullName IS NULL OR LOWER(gb.fullName) LIKE LOWER(CONCAT('%', CAST(:fullName AS string), '%')))
              AND (:phoneNumber IS NULL OR gb.phoneNumber LIKE CONCAT('%', CAST(:phoneNumber AS string), '%'))
              AND (:customerGroup IS NULL OR LOWER(COALESCE(cg.groupName, '')) LIKE LOWER(CONCAT('%', CAST(:customerGroup AS string), '%')))
            """)
    Page<GasBookEntity> searchGasBooks(
            @Param("keyword") String keyword,
            @Param("customerCode") String customerCode,
            @Param("fullName") String fullName,
            @Param("phoneNumber") String phoneNumber,
            @Param("customerGroup") String customerGroup,
            Pageable pageable);

    @Query("SELECT CAST(s.gasBook.gasBookId AS string) AS gasBookCode, s.gasBook.fullName AS customerName, " +
           "COALESCE(SUM(s.totalAmount), 0) AS totalAmount, " +
           "COALESCE(SUM(s.discountAmount), 0) AS totalDiscount, " +
           "COALESCE(SUM(s.totalAmount - COALESCE(s.discountAmount, 0)), 0) AS totalAfterDiscount, " +
           "COALESCE(SUM(s.paidAmount), 0) AS totalPaid, " +
           "COALESCE(SUM(s.totalAmount - COALESCE(s.discountAmount, 0) - COALESCE(s.paidAmount, 0)), 0) AS totalDebt " +
           "FROM SaleInvoiceEntity s " +
           "WHERE s.gasBook IS NOT NULL " +
           "AND s.invoiceDate >= :startDate AND s.invoiceDate <= :endDate " +
           "GROUP BY s.gasBook.gasBookId, s.gasBook.fullName " +
           "ORDER BY s.gasBook.fullName ASC")
    List<GasBookReportDTO> getGasBookReport(@Param("startDate") OffsetDateTime startDate, @Param("endDate") OffsetDateTime endDate);
}
