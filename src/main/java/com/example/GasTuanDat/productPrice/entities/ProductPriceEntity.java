package com.example.GasTuanDat.productPrice.entities;

import java.math.BigDecimal;
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
@Data
@Table(name = "\"ProductPrice\"",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_price_product",
            columnNames = {"priceListId", "productId"}
        )
       }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPriceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "\"id\"", nullable = false)
    private UUID id;

    @JoinColumn(name = "\"priceListId\"", nullable = false)
    @ManyToOne
    private PriceListEntity priceList;

    @JoinColumn(name = "\"productId\"", nullable = false)
    @ManyToOne
    private ProductEntity product;

    @Column(name = "\"sellingPrice\"", nullable = false)
    private BigDecimal sellingPrice;
}
