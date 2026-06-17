package com.example.GasTuanDat.supplier;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.GasTuanDat.common.exception.ApiException;
import com.example.GasTuanDat.common.exception.ErrorCode;
import com.example.GasTuanDat.common.response.PageResult;
import com.example.GasTuanDat.supplier.dtos.SupplierCreateRequest;
import com.example.GasTuanDat.supplier.dtos.SupplierResponse;
import com.example.GasTuanDat.supplier.dtos.SupplierUpdateRequest;
import com.example.GasTuanDat.supplier.entities.SupplierEntity;
import com.example.GasTuanDat.supplier.mapper.SupplierMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    /**
     * Create new supplier
     * Validates: name not empty, phone format valid, phone not duplicate
     */
    public SupplierResponse create(SupplierCreateRequest request) {
        // Validate name is not empty
        if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
            throw new ApiException(ErrorCode.INVALID_INPUT);
        }

        // Check if phone already exists
        Optional<SupplierEntity> existingSupplier = supplierRepository.findByPhoneNumber(request.getPhoneNumber());
        if (existingSupplier.isPresent()) {
            throw new ApiException(ErrorCode.INVALID_INPUT);
        }

        SupplierEntity supplier = supplierMapper.toEntity(request);
        SupplierEntity saved = supplierRepository.save(supplier);
        return supplierMapper.toResponse(saved);
    }

    /**
     * Get supplier by ID
     */
    public SupplierResponse getById(UUID supplierId) {
        SupplierEntity supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ApiException(ErrorCode.SUPPLIER_NOT_FOUND));
        return supplierMapper.toResponse(supplier);
    }

    /**
     * Update supplier information
     */
    public SupplierResponse update(UUID supplierId, SupplierUpdateRequest request) {
        SupplierEntity supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ApiException(ErrorCode.SUPPLIER_NOT_FOUND));

        // If updating phone, check for duplicates (excluding current supplier)
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().equals(supplier.getPhoneNumber())) {
            Optional<SupplierEntity> existingSupplier = supplierRepository.findByPhoneNumber(request.getPhoneNumber());
            if (existingSupplier.isPresent()) {
                throw new ApiException(ErrorCode.INVALID_INPUT);
            }
        }

        supplierMapper.updateEntity(request, supplier);
        SupplierEntity updated = supplierRepository.save(supplier);
        return supplierMapper.toResponse(updated);
    }

    /**
     * Delete supplier by ID
     */
    public void delete(UUID supplierId) {
        SupplierEntity supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ApiException(ErrorCode.SUPPLIER_NOT_FOUND));
        supplierRepository.delete(supplier);
    }

    /**
     * Search suppliers with filters and pagination
     * Supports search by keyword
     */
    public PageResult<SupplierResponse> search(
            String keyword,
            Integer page,
            Integer limit
    ) {

        int pageNumber = page == null || page < 1 ? 0 : page - 1;
        int pageSize = limit == null || limit <= 0 ? 10 : limit;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("fullName").ascending());

        Page<SupplierEntity> supplierPage = supplierRepository.searchSuppliers(keyword, pageable);

        return PageResult.<SupplierResponse>builder()
                .content(supplierPage.getContent().stream()
                        .map(supplierMapper::toResponse)
                        .toList())
                .page(supplierPage.getNumber() + 1)
                .size(supplierPage.getSize())
                .totalElements(supplierPage.getTotalElements())
                .totalPages(supplierPage.getTotalPages())
                .build();
    }
}
