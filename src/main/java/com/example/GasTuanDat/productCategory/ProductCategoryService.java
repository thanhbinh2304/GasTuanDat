package com.example.GasTuanDat.productCategory;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.GasTuanDat.common.exception.ApiException;
import com.example.GasTuanDat.common.exception.ErrorCode;
import com.example.GasTuanDat.productCategory.dtos.ProductCategoryCreateRequest;
import com.example.GasTuanDat.productCategory.dtos.ProductCategoryUpdateRequest;
import com.example.GasTuanDat.productCategory.entities.ProductCategoryEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {
    private final ProductCategoryRepository repository;

    public ProductCategoryEntity create(ProductCategoryCreateRequest request) {
        String name = request.getCategoryName().trim();
        if (repository.existsByCategoryNameIgnoreCase(name)) {
            throw new ApiException(ErrorCode.PRODUCT_CATEGORY_ALREADY_EXISTS);
        }

        ProductCategoryEntity entity = new ProductCategoryEntity();
        entity.setCategoryName(name);
        return repository.save(entity);
    }

    public List<ProductCategoryEntity> getAll() {
        return repository.findAll();
    }

    public ProductCategoryEntity getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_CATEGORY_NOT_FOUND));
    }

    public ProductCategoryEntity update(UUID id, ProductCategoryUpdateRequest request) {
        ProductCategoryEntity entity = getById(id);
        String newName = request.getCategoryName();
        if (newName != null) {
            newName = newName.trim();
            if (newName.isBlank()) {
                throw new ApiException(ErrorCode.INVALID_INPUT);
            }
            if (!newName.equalsIgnoreCase(entity.getCategoryName())
                    && repository.existsByCategoryNameIgnoreCase(newName)) {
                throw new ApiException(ErrorCode.PRODUCT_CATEGORY_ALREADY_EXISTS);
            }
            entity.setCategoryName(newName);
        }
        return repository.save(entity);
    }

    public void delete(UUID id) {
        ProductCategoryEntity entity = getById(id);
        repository.delete(entity);
    }

    public Page<ProductCategoryEntity> search(String categoryName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (categoryName == null || categoryName.isBlank()) {
            return repository.findAll(pageable);
        }
        return repository.findByCategoryNameContainingIgnoreCase(categoryName.trim(), pageable);
    }
}
