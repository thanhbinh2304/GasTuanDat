package com.example.GasTuanDat.stockTransfer.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.GasTuanDat.product.entities.ProductEntity;

@Data
@Entity
@Table(name = "\"StockTransferDetail\"",
       uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_stocktransfer_product",
            columnNames = {"transferId", "productId"}
        )
       }
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockTransferDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "\"id\"", nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"transferId\"")
    private StockTransferEntity transfer;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"productId\"")
    private ProductEntity product;

    @Column(name = "\"quantity\"")
    private Short quantity;
}
