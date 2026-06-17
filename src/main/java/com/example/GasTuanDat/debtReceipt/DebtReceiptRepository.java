package com.example.GasTuanDat.debtReceipt;

import java.time.LocalDate;
import java.util.UUID;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.GasTuanDat.debtReceipt.entities.DebtReceiptEntity;

@Repository
public interface DebtReceiptRepository extends JpaRepository<DebtReceiptEntity, UUID> {

    boolean existsByReceiptCodeIgnoreCase(String receiptCode);

    @Query("SELECT d.receiptCode FROM DebtReceiptEntity d WHERE d.receiptCode LIKE concat(:prefix, '%')")
    List<String> findReceiptCodesByPrefix(@Param("prefix") String prefix);

    @Query("""
            SELECT d FROM DebtReceiptEntity d
            LEFT JOIN d.customer c
            LEFT JOIN d.gasBook g
            WHERE (:keyword IS NULL OR LOWER(d.receiptCode) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')) OR LOWER(c.fullName) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')) OR LOWER(g.fullName) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')))
              AND (cast(:startDate as date) IS NULL OR d.debtDate >= :startDate)
              AND (cast(:endDate as date) IS NULL OR d.debtDate <= :endDate)
              AND (cast(:dueStartDate as date) IS NULL OR d.dueDate >= :dueStartDate)
              AND (cast(:dueEndDate as date) IS NULL OR d.dueDate <= :dueEndDate)
            """)
    Page<DebtReceiptEntity> searchDebtReceipts(
            @Param("keyword") String keyword,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("dueStartDate") LocalDate dueStartDate,
            @Param("dueEndDate") LocalDate dueEndDate,
            Pageable pageable);
}
