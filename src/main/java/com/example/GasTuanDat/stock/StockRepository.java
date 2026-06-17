package com.example.GasTuanDat.stock;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.GasTuanDat.stock.entities.StockEntity;

public interface StockRepository extends JpaRepository<StockEntity, UUID> {
    boolean existsByNameIgnoreCase(String name);

    Page<StockEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Optional<StockEntity> findByNameIgnoreCase(String name);
}