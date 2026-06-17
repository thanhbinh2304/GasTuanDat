package com.example.GasTuanDat.stock.entities;

import java.util.UUID;

import com.example.GasTuanDat.product.entities.ProductEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"Inventory\"", uniqueConstraints = {
        @UniqueConstraint(name = "uk_stock_product", columnNames = { "stockId", "productId" })
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "\"inventoryId\"", nullable = false)
    private UUID inventoryId;

    @JoinColumn(name = "\"stockId\"", nullable = false)
    @ManyToOne
    private StockEntity stock;

    @JoinColumn(name = "\"productId\"", nullable = false)
    @ManyToOne
    private ProductEntity product;

    @Column(name = "\"quantity\"", nullable = false)
    private Integer quantity;
}
