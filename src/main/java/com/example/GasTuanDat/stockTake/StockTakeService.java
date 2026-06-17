package com.example.GasTuanDat.stockTake;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.GasTuanDat.employee.EmployeeRepository;
import com.example.GasTuanDat.employee.entities.EmployeeEntity;
import com.example.GasTuanDat.common.exception.ApiException;
import com.example.GasTuanDat.common.exception.ErrorCode;
import com.example.GasTuanDat.product.ProductRepository;
import com.example.GasTuanDat.product.entities.ProductEntity;
import com.example.GasTuanDat.stock.InventoryRepository;
import com.example.GasTuanDat.stock.StockRepository;
import com.example.GasTuanDat.stock.entities.InventoryEntity;
import com.example.GasTuanDat.stock.entities.StockEntity;
import com.example.GasTuanDat.stockTake.dtos.StockTakeCreateRequest;
import com.example.GasTuanDat.stockTake.dtos.StockTakeDetailCreateRequest;
import com.example.GasTuanDat.stockTake.dtos.StockTakeDetailResponse;
import com.example.GasTuanDat.stockTake.dtos.StockTakeDetailUpdateRequest;
import com.example.GasTuanDat.stockTake.dtos.StockTakeResponse;
import com.example.GasTuanDat.stockTake.dtos.StockTakeUpdateRequest;
import com.example.GasTuanDat.stockTake.entities.StockTakeDetailEntity;
import com.example.GasTuanDat.stockTake.entities.StockTakeEntity;
import com.example.GasTuanDat.stockTake.mapper.StockTakeDetailMapper;
import com.example.GasTuanDat.stockTake.mapper.StockTakeMapper;


@Service
public class StockTakeService {

    @Autowired
    private StockTakeRepository stockTakeRepository;

    @Autowired
    private StockTakeDetailRepository stockTakeDetailRepository;

    @Autowired
    private StockTakeMapper stockTakeMapper;

    @Autowired
    private StockTakeDetailMapper stockTakeDetailMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    public Page<StockTakeResponse> search(
            String keyword,
            OffsetDateTime startDate,
            OffsetDateTime endDate,
            String stock,
            String employee,
            int page,
            int pageSize) {

        String normalizedKeyword = normalizeFilter(keyword);
        String normalizedStock = normalizeFilter(stock);
        String normalizedEmployee = normalizeFilter(employee);

        UUID stockId = tryParseUuid(normalizedStock);
        String stockName = stockId == null ? normalizedStock : null;

        UUID employeeId = tryParseUuid(normalizedEmployee);
        String employeeName = employeeId == null ? normalizedEmployee : null;

        Pageable pageable = PageRequest.of(page, pageSize);

        Page<StockTakeEntity> pagedResult = stockTakeRepository.searchByFilters(
                normalizedKeyword,
                startDate,
                endDate,
                stockId,
                stockName,
                employeeId,
                employeeName,
                pageable);

        return pagedResult.map(this::mapToResponseWithDetails);
    }

    public StockTakeResponse getById(UUID id) {
        StockTakeEntity entity = stockTakeRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.STOCK_TAKE_NOT_FOUND));
        return mapToResponseWithDetails(entity);
    }

    @Transactional
    public StockTakeResponse create(StockTakeCreateRequest request) {
        StockTakeEntity entity = stockTakeMapper.toEntity(request);

        String stockTakeCode = generateStockTakeCode();
        entity.setStockTakeCode(stockTakeCode);

        if (request.getEmployeeId() != null) {
            EmployeeEntity emp = employeeRepository.findById(request.getEmployeeId())
                    .orElseThrow(() -> new ApiException(ErrorCode.EMPLOYEE_NOT_FOUND));
            entity.setEmployee(emp);
        }

        if (request.getStockId() != null) {
            StockEntity stock = stockRepository.findById(request.getStockId())
                    .orElseThrow(() -> new ApiException(ErrorCode.STOCK_NOT_FOUND));
            entity.setStock(stock);
        }

        entity = stockTakeRepository.save(entity);

        if (request.getDetails() != null && !request.getDetails().isEmpty()) {
            for (StockTakeDetailCreateRequest detailReq : request.getDetails()) {
                StockTakeDetailEntity detail = stockTakeDetailMapper.toEntity(detailReq);
                detail.setStockTake(entity);

                ProductEntity product = productRepository.findById(detailReq.getProductId())
                        .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
                detail.setProduct(product);

                stockTakeDetailRepository.save(detail);

                if (entity.getStock() != null && detailReq.getActualQuantity() != null) {
                    adjustInventory(entity.getStock(), product, detailReq.getActualQuantity().intValue());
                }
            }
        }

        return mapToResponseWithDetails(entity);
    }

    @Transactional
    public StockTakeResponse update(UUID id, StockTakeUpdateRequest request) {
        StockTakeEntity entity = stockTakeRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.STOCK_TAKE_NOT_FOUND));

        // Revert old inventory logic
        revertInventory(entity);

        stockTakeMapper.updateEntity(request, entity);

        if (request.getEmployeeId() != null) {
            EmployeeEntity emp = employeeRepository.findById(request.getEmployeeId())
                    .orElseThrow(() -> new ApiException(ErrorCode.EMPLOYEE_NOT_FOUND));
            entity.setEmployee(emp);
        } else {
            entity.setEmployee(null);
        }

        if (request.getStockId() != null) {
            StockEntity stock = stockRepository.findById(request.getStockId())
                    .orElseThrow(() -> new ApiException(ErrorCode.STOCK_NOT_FOUND));
            entity.setStock(stock);
        } else {
            entity.setStock(null);
        }

        entity = stockTakeRepository.save(entity);

        stockTakeDetailRepository.deleteByStockTakeStockTakeId(entity.getStockTakeId());
        stockTakeDetailRepository.flush();

        if (request.getDetails() != null && !request.getDetails().isEmpty()) {
            for (StockTakeDetailUpdateRequest detailReq : request.getDetails()) {
                StockTakeDetailEntity detail = new StockTakeDetailEntity();
                stockTakeDetailMapper.updateEntity(detailReq, detail);
                detail.setStockTake(entity);

                ProductEntity product = productRepository.findById(detailReq.getProductId())
                        .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
                detail.setProduct(product);

                stockTakeDetailRepository.save(detail);

                if (entity.getStock() != null && detailReq.getActualQuantity() != null) {
                    adjustInventory(entity.getStock(), product, detailReq.getActualQuantity().intValue());
                }
            }
        }

        return mapToResponseWithDetails(entity);
    }

    @Transactional
    public void delete(UUID id) {
        StockTakeEntity entity = stockTakeRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.STOCK_TAKE_NOT_FOUND));
        
        revertInventory(entity);
        stockTakeDetailRepository.deleteByStockTakeStockTakeId(id);
        stockTakeRepository.delete(entity);
    }

    private void revertInventory(StockTakeEntity stockTake) {
        if (stockTake.getStock() == null) return;
        List<StockTakeDetailEntity> details = stockTakeDetailRepository.findByStockTakeStockTakeId(stockTake.getStockTakeId());
        for (StockTakeDetailEntity detail : details) {
            if (detail.getSystemQuantity() != null) {
                // To revert, we set the inventory back to what the system quantity was
                adjustInventory(stockTake.getStock(), detail.getProduct(), detail.getSystemQuantity().intValue());
            }
        }
    }

    private void adjustInventory(StockEntity stock, ProductEntity product, int newQuantity) {
        InventoryEntity inventory = inventoryRepository.findByStockStockIdAndProductProductId(stock.getStockId(), product.getProductId())
                .orElseGet(() -> {
                    InventoryEntity newInv = new InventoryEntity();
                    newInv.setStock(stock);
                    newInv.setProduct(product);
                    newInv.setQuantity(0);
                    return inventoryRepository.save(newInv);
                });
        inventory.setQuantity(newQuantity);
        inventoryRepository.save(inventory);
    }

    private String generateStockTakeCode() {
        long currentCount = stockTakeRepository.count();
        String prefix = "PK";
        String generatedCode;
        do {
            currentCount++;
            generatedCode = String.format("%s%05d", prefix, currentCount);
        } while (stockTakeRepository.existsByStockTakeCodeIgnoreCase(generatedCode));
        return generatedCode;
    }

    private StockTakeResponse mapToResponseWithDetails(StockTakeEntity entity) {
        StockTakeResponse response = stockTakeMapper.toResponse(entity);
        
        if (entity.getEmployee() != null) {
            response.setEmployeeName(entity.getEmployee().getFullName());
        }
        if (entity.getStock() != null) {
            response.setStockName(entity.getStock().getName());
        }

        List<StockTakeDetailEntity> detailEntities = stockTakeDetailRepository.findByStockTakeStockTakeId(entity.getStockTakeId());
        List<StockTakeDetailResponse> detailResponses = detailEntities.stream()
                .map(stockTakeDetailMapper::toResponse)
                .collect(Collectors.toList());
        
        response.setDetails(detailResponses);
        return response;
    }

    private String normalizeFilter(String filter) {
        return (filter != null && !filter.trim().isEmpty()) ? filter.trim() : null;
    }

    private UUID tryParseUuid(String value) {
        if (value == null) return null;
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
