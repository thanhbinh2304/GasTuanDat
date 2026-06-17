package com.example.GasTuanDat.debtReceipt;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GasTuanDat.debtReceipt.entities.DebtReceiptDetailEntity;

@Repository
public interface DebtReceiptDetailRepository extends JpaRepository<DebtReceiptDetailEntity, UUID> {
    List<DebtReceiptDetailEntity> findByDebtReceipt_ReceiptId(UUID receiptId);
}
