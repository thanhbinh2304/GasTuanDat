package com.example.GasTuanDat.purchase.entities;

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

@Data
@Entity
@Table(name = "\"PurchaseDetail\"",
       uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_purchase_product",
            columnNames = {"purchaseId", "productId"}
        )
       } 
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "\"id\"", nullable = false)
    private UUID id;

    @JoinColumn(name = "\"purchaseId\"", nullable = false)
    @ManyToOne
    private PurchaseOrderEntity purchase;

    @JoinColumn(name = "\"productId\"", nullable = false)
    @ManyToOne
    private ProductEntity product;

    @Column(name = "\"quantity\"", nullable = false)
    private Integer quantity;

    @Column(name = "\"purchasePrice\"", nullable = false)
    private BigDecimal purchasePrice;

    @Column(name = "\"total\"", nullable = false)
    private BigDecimal total;
}
