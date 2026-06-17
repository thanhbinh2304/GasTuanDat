package com.example.GasTuanDat.productCategory.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "\"ProductCategory\"")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "\"categoryId\"", nullable = false)
    private UUID categoryId;

    @Column(name = "\"categoryName\"", nullable = false, columnDefinition = "text")
    private String categoryName;
}
