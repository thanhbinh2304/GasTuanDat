package com.example.GasTuanDat.product.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.GasTuanDat.productCategory.entities.ProductCategoryEntity;

import com.example.GasTuanDat.productCategory.entities.ProductCategoryEntity;

@Data
@Entity
@Table(name = "\"Product\"")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "\"productId\"", nullable = false)
    private UUID productId;

    @NotNull
    @Column(name = "\"productCode\"", nullable = false, unique = true)
    private String productCode;

    @NotNull
    @Column(name = "\"productName\"", nullable = false, columnDefinition = "text")
    private String productName;

    @Column(name = "\"cost\"", nullable = false)
    private BigDecimal cost;

    @ManyToOne
    @JoinColumn(name = "\"categoryId\"", nullable = false)
    private ProductCategoryEntity category;

    @Column(name = "\"unit\"")
    private Short unit;

    @Column(name = "\"note\"", columnDefinition = "text")
    private String note;

    @jakarta.persistence.OneToMany(mappedBy = "product", fetch = jakarta.persistence.FetchType.LAZY)
    @org.hibernate.annotations.BatchSize(size = 50)
    private java.util.List<com.example.GasTuanDat.stock.entities.InventoryEntity> inventoryList;

    @jakarta.persistence.OneToMany(mappedBy = "product", fetch = jakarta.persistence.FetchType.LAZY)
    @org.hibernate.annotations.BatchSize(size = 50)
    private java.util.List<com.example.GasTuanDat.productPrice.entities.ProductPriceEntity> priceTiers;

    @jakarta.persistence.OneToMany(mappedBy = "product", fetch = jakarta.persistence.FetchType.LAZY)
    @org.hibernate.annotations.BatchSize(size = 50)
    private java.util.List<com.example.GasTuanDat.productAttribute.entities.ProductAttributeEntity> attributesList;
}
