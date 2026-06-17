package com.example.GasTuanDat.stockTransfer;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GasTuanDat.stockTransfer.entities.StockTransferDetailEntity;

@Repository
public interface StockTransferDetailRepository extends JpaRepository<StockTransferDetailEntity, UUID> {
    List<StockTransferDetailEntity> findByTransferTransferId(UUID transferId);
    void deleteByTransferTransferId(UUID transferId);
}