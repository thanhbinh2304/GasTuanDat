package com.example.GasTuanDat.stock;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.GasTuanDat.stock.entities.InventoryEntity;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, UUID> {
    List<InventoryEntity> findByProductProductId(UUID productId);
    void deleteByProductProductId(UUID productId);
    java.util.Optional<InventoryEntity> findByStockStockIdAndProductProductId(UUID stockId, UUID productId);
}
