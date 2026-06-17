package com.example.GasTuanDat.stockTake.entities;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.GasTuanDat.product.entities.ProductEntity;
import com.example.GasTuanDat.stockTransfer.entities.StockTransferEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Entity
@Table(name = "\"StockTakeDetail\"",
       uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_stocktake_product",
            columnNames = {"stockTakeId", "productId"}
        )
       }
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockTakeDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "\"id\"", nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"stockTakeId\"")
    private StockTakeEntity stockTake;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"productId\"")
    private ProductEntity product;

    @Column(name = "\"systymQuantity\"")
    private Short systemQuantity;

    @Column(name = "\"actualQuantity\"")
    private Short actualQuantity;
}
