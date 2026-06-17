package com.example.GasTuanDat.productCategory;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GasTuanDat.productCategory.entities.ProductCategoryEntity;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, UUID> {
    boolean existsByCategoryNameIgnoreCase(String categoryName);

    Optional<ProductCategoryEntity> findByCategoryNameIgnoreCase(String categoryName);

    Page<ProductCategoryEntity> findByCategoryNameContainingIgnoreCase(String categoryName, Pageable pageable);
}
