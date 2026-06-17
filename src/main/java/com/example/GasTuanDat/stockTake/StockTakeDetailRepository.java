package com.example.GasTuanDat.stockTake;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GasTuanDat.stockTake.entities.StockTakeDetailEntity;

@Repository
public interface StockTakeDetailRepository extends JpaRepository<StockTakeDetailEntity, UUID> {
    List<StockTakeDetailEntity> findByStockTakeStockTakeId(UUID stockTakeId);
    void deleteByStockTakeStockTakeId(UUID stockTakeId);
}
