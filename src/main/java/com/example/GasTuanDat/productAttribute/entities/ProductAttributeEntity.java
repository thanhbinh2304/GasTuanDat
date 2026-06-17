package com.example.GasTuanDat.productAttribute.entities;

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
@Table(name = "\"ProductAttribute\"",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_attribute_product",
            columnNames = {"attributeId", "productId"}
        )
       }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductAttributeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "\"id\"", nullable = false)
    private UUID id;

    @JoinColumn(name = "\"attributeId\"", nullable = false)
    @ManyToOne
    private AttributeEntity attribute;

    @JoinColumn(name = "\"productId\"", nullable = false)
    @ManyToOne
    private ProductEntity product;

    @Column(name = "\"attributeValue\"", nullable = false)
    private String attributeValue;
}
