package com.example.GasTuanDat.stockTransfer;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.GasTuanDat.common.exception.ApiException;
import com.example.GasTuanDat.common.exception.ErrorCode;
import com.example.GasTuanDat.employee.EmployeeRepository;
import com.example.GasTuanDat.employee.entities.EmployeeEntity;
import com.example.GasTuanDat.product.ProductRepository;
import com.example.GasTuanDat.product.entities.ProductEntity;
import com.example.GasTuanDat.stock.InventoryRepository;
import com.example.GasTuanDat.stock.StockRepository;
import com.example.GasTuanDat.stock.entities.StockEntity;
import com.example.GasTuanDat.stock.entities.InventoryEntity;
import com.example.GasTuanDat.stockTransfer.dtos.StockTransferCreateRequest;
import com.example.GasTuanDat.stockTransfer.dtos.StockTransferDetailCreateRequest;
import com.example.GasTuanDat.stockTransfer.dtos.StockTransferUpdateRequest;
import com.example.GasTuanDat.stockTransfer.entities.StockTransferDetailEntity;
import com.example.GasTuanDat.stockTransfer.entities.StockTransferEntity;

import lombok.RequiredArgsConstructor;

import com.example.GasTuanDat.stock.InventoryRepository;
import com.example.GasTuanDat.stock.entities.InventoryEntity;

@Service
@RequiredArgsConstructor
public class StockTransferService {
    private final StockTransferRepository stockTransferRepository;
    private final StockTransferDetailRepository stockTransferDetailRepository;
    private final StockRepository stockRepository;
    private final EmployeeRepository employeeRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    @Transactional
    public StockTransferEntity create(StockTransferCreateRequest request) {
        String transferCode = request.getTransferCode();
        if (transferCode == null || transferCode.trim().isEmpty()) {
            transferCode = generateTransferCode();
        } else {
            validateTransferCode(transferCode);
        }

        StockEntity fromStock = null;
        if (request.getFromStockId() != null) {
            fromStock = stockRepository.findById(request.getFromStockId())
                    .orElseThrow(() -> new ApiException(ErrorCode.STOCK_NOT_FOUND));
        }

        StockEntity toStock = null;
        if (request.getToStockId() != null) {
            toStock = stockRepository.findById(request.getToStockId())
                    .orElseThrow(() -> new ApiException(ErrorCode.STOCK_NOT_FOUND));
        }

        EmployeeEntity employee = null;
        if (request.getEmployeeId() != null) {
            employee = employeeRepository.findById(request.getEmployeeId())
                    .orElseThrow(() -> new ApiException(ErrorCode.EMPLOYEE_NOT_FOUND));
        }

        StockTransferEntity transfer = StockTransferEntity.builder()
                .transferCode(transferCode)
                .transferDate(request.getTransferDate())
                .note(request.getNote())
                .fromStock(fromStock)
                .toStock(toStock)
                .employee(employee)
                .details(new java.util.ArrayList<>())
                .build();

        StockTransferEntity saved = stockTransferRepository.save(transfer);

        // Save transfer details
        if (request.getDetails() != null) {
            java.util.Map<UUID, Short> productQuantityMap = new java.util.HashMap<>();
            for (StockTransferDetailCreateRequest detail : request.getDetails()) {
                if (detail.getProductId() != null) {
                    productQuantityMap.merge(detail.getProductId(), detail.getQuantity(), (a, b) -> (short)(a + b));
                }
            }

            for (java.util.Map.Entry<UUID, Short> entry : productQuantityMap.entrySet()) {
                ProductEntity product = productRepository.findById(entry.getKey())
                        .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
                StockTransferDetailEntity transferDetail = StockTransferDetailEntity.builder()
                        .transfer(saved)
                        .product(product)
                        .quantity(entry.getValue())
                        .build();
                saved.getDetails().add(transferDetail);
                
                adjustInventory(fromStock, product, -entry.getValue());
                adjustInventory(toStock, product, entry.getValue());
            }
            stockTransferRepository.save(saved);
        }

        return saved;
    }

    public StockTransferEntity getById(UUID transferId) {
        return stockTransferRepository.findById(transferId)
                .orElseThrow(() -> new ApiException(ErrorCode.TRANSFER_NOT_FOUND));
    }

    public Page<StockTransferEntity> search(
            String keyword,
            String startDate,
            String endDate,
            String fromStock,
            String toStock,
            String creator,
            int page,
            int pageSize) {
        String normalizedKeyword = normalizeFilter(keyword);
        String normalizedStartDate = normalizeFilter(startDate);
        String normalizedEndDate = normalizeFilter(endDate);
        String normalizedFromStock = normalizeFilter(fromStock);
        String normalizedToStock = normalizeFilter(toStock);
        String normalizedCreator = normalizeFilter(creator);

        UUID fromStockId = tryParseUuid(normalizedFromStock);
        String fromStockName = fromStockId == null ? normalizedFromStock : null;

        UUID toStockId = tryParseUuid(normalizedToStock);
        String toStockName = toStockId == null ? normalizedToStock : null;

        UUID employeeId = tryParseUuid(normalizedCreator);
        String employeeName = employeeId == null ? normalizedCreator : null;

        Pageable pageable = PageRequest.of(page, pageSize);

        return stockTransferRepository.searchByFilters(
                normalizedKeyword,
                normalizedStartDate,
                normalizedEndDate,
                fromStockId,
                fromStockName,
                toStockId,
                toStockName,
                employeeId,
                employeeName,
                pageable);
    }

    @Transactional
    public StockTransferEntity update(UUID transferId, StockTransferUpdateRequest request) {
        StockTransferEntity transfer = getById(transferId);

        // Revert old inventory changes before updating
        if (transfer.getDetails() != null) {
            for (StockTransferDetailEntity oldDetail : transfer.getDetails()) {
                adjustInventory(transfer.getFromStock(), oldDetail.getProduct(), oldDetail.getQuantity());
                adjustInventory(transfer.getToStock(), oldDetail.getProduct(), -oldDetail.getQuantity());
            }
        }

        if (request.getTransferCode() != null) {
            String code = request.getTransferCode().trim();
            if (!code.equalsIgnoreCase(transfer.getTransferCode())) {
                if (stockTransferRepository.existsByTransferCodeIgnoreCase(code)) {
                    throw new ApiException(ErrorCode.TRANSFER_CODE_ALREADY_EXISTS);
                }
                transfer.setTransferCode(code);
            }
        }

        if (request.getTransferDate() != null) {
            transfer.setTransferDate(request.getTransferDate());
        }

        if (request.getNote() != null) {
            transfer.setNote(request.getNote());
        }

        if (request.getFromStockId() != null) {
            StockEntity fromStock = stockRepository.findById(request.getFromStockId())
                    .orElseThrow(() -> new ApiException(ErrorCode.STOCK_NOT_FOUND));
            transfer.setFromStock(fromStock);
        }

        if (request.getToStockId() != null) {
            StockEntity toStock = stockRepository.findById(request.getToStockId())
                    .orElseThrow(() -> new ApiException(ErrorCode.STOCK_NOT_FOUND));
            transfer.setToStock(toStock);
        }

        if (request.getEmployeeId() != null) {
            EmployeeEntity employee = employeeRepository.findById(request.getEmployeeId())
                    .orElseThrow(() -> new ApiException(ErrorCode.EMPLOYEE_NOT_FOUND));
            transfer.setEmployee(employee);
        }

        // Update transfer details - delete all and recreate
        if (request.getDetails() != null) {
            if (transfer.getDetails() != null) {
                transfer.getDetails().clear();
                stockTransferRepository.saveAndFlush(transfer);
            } else {
                transfer.setDetails(new java.util.ArrayList<>());
            }
            
            java.util.Map<UUID, Short> productQuantityMap = new java.util.HashMap<>();
            for (StockTransferDetailCreateRequest detail : request.getDetails()) {
                if (detail.getProductId() != null) {
                    productQuantityMap.merge(detail.getProductId(), detail.getQuantity(), (a, b) -> (short)(a + b));
                }
            }

            for (java.util.Map.Entry<UUID, Short> entry : productQuantityMap.entrySet()) {
                ProductEntity product = productRepository.findById(entry.getKey())
                        .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
                StockTransferDetailEntity transferDetail = StockTransferDetailEntity.builder()
                        .transfer(transfer)
                        .product(product)
                        .quantity(entry.getValue())
                        .build();
                transfer.getDetails().add(transferDetail);
                
                adjustInventory(transfer.getFromStock(), product, -entry.getValue());
                adjustInventory(transfer.getToStock(), product, entry.getValue());
            }
        }

        return stockTransferRepository.save(transfer);
    }

    @Transactional
    public void delete(UUID transferId) {
        StockTransferEntity transfer = getById(transferId);
        
        // Revert inventory changes before deleting
        if (transfer.getDetails() != null) {
            for (StockTransferDetailEntity oldDetail : transfer.getDetails()) {
                adjustInventory(transfer.getFromStock(), oldDetail.getProduct(), oldDetail.getQuantity());
                adjustInventory(transfer.getToStock(), oldDetail.getProduct(), -oldDetail.getQuantity());
            }
        }
        
        stockTransferDetailRepository.deleteByTransferTransferId(transferId);
        stockTransferRepository.delete(transfer);
    }

    private void adjustInventory(StockEntity stock, ProductEntity product, int quantityDelta) {
        if (stock == null || product == null || quantityDelta == 0) return;
        InventoryEntity inventory = inventoryRepository.findByStockStockIdAndProductProductId(stock.getStockId(), product.getProductId())
                .orElse(InventoryEntity.builder().stock(stock).product(product).quantity(0).build());
        inventory.setQuantity((inventory.getQuantity() != null ? inventory.getQuantity() : 0) + quantityDelta);
        inventoryRepository.save(inventory);
    }

    private String generateTransferCode() {
        String prefix = "DC";
        long count = stockTransferRepository.count();
        String code = prefix + String.format("%05d", count + 1);
        while (stockTransferRepository.existsByTransferCodeIgnoreCase(code)) {
            count++;
            code = prefix + String.format("%05d", count + 1);
        }
        return code;
    }

    private void validateTransferCode(String transferCode) {
        if (transferCode == null || transferCode.trim().isEmpty()) {
            throw new ApiException(ErrorCode.INVALID_INPUT);
        }
        if (stockTransferRepository.existsByTransferCodeIgnoreCase(transferCode.trim())) {
            throw new ApiException(ErrorCode.TRANSFER_CODE_ALREADY_EXISTS);
        }
    }

    private String normalizeFilter(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim();
        if (normalized.isEmpty() || "all".equalsIgnoreCase(normalized) || "Tất cả".equalsIgnoreCase(normalized)) {
            return null;
        }
        return normalized;
    }

    private UUID tryParseUuid(String value) {
        if (value == null) {
            return null;
        }
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}