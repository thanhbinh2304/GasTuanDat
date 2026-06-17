package com.example.GasTuanDat.purchase;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.GasTuanDat.common.exception.ApiException;
import com.example.GasTuanDat.common.exception.ErrorCode;
import com.example.GasTuanDat.common.response.PageResult;
import com.example.GasTuanDat.employee.EmployeeRepository;
import com.example.GasTuanDat.purchase.dtos.PurchaseOrderCreateRequest;
import com.example.GasTuanDat.purchase.dtos.PurchaseOrderResponse;
import com.example.GasTuanDat.purchase.dtos.PurchaseOrderUpdateRequest;
import com.example.GasTuanDat.purchase.entities.PurchaseOrderEntity;
import com.example.GasTuanDat.purchase.mapper.PurchaseOrderMapper;
import com.example.GasTuanDat.stock.StockRepository;
import com.example.GasTuanDat.stock.entities.StockEntity;
import com.example.GasTuanDat.supplier.SupplierRepository;
import com.example.GasTuanDat.supplier.entities.SupplierEntity;
import com.example.GasTuanDat.employee.entities.EmployeeEntity;
import com.example.GasTuanDat.stock.InventoryRepository;
import com.example.GasTuanDat.stock.entities.InventoryEntity;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;
    private final EmployeeRepository employeeRepository;
    private final SupplierRepository supplierRepository;
    private final StockRepository stockRepository;
    private final PurchaseDetailRepository purchaseDetailRepository;
    private final InventoryRepository inventoryRepository;
    private final org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @jakarta.annotation.PostConstruct
    public void printEnumValues() {
        try {
            java.util.List<String> enums = jdbcTemplate.queryForList("SELECT enum_range(NULL::ordertype)::varchar", String.class);
            System.out.println("====== ENUM VALUES FOR ordertype ======");
            System.out.println(enums);
            System.out.println("=======================================");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @CacheEvict(value = {"purchaseOrders", "reports"}, allEntries = true)
    public PurchaseOrderResponse create(PurchaseOrderCreateRequest request) {
        PurchaseOrderEntity entity = purchaseOrderMapper.toEntity(request);
        if (entity.getPurchaseDate() == null) {
            entity.setPurchaseDate(OffsetDateTime.now());
        }
        entity.setPurchaseCode(generatePurchaseCode());
        if (request.getOrderType() != null) {
            entity.setOrderType(request.getOrderType());
        } else {
            entity.setOrderType("Dathang");
        }
        entity.setEmployee(getEmployeeOrNull(request.getEmployeeId()));
        entity.setSupplier(getSupplierOrNull(request.getSupplierId()));
        entity.setStock(getStockOrNull(request.getStockId()));
        return purchaseOrderMapper.toResponse(purchaseOrderRepository.save(entity));
    }

    private String generatePurchaseCode() {
        long currentCount = purchaseOrderRepository.count();
        String prefix = "DHN";
        String generatedCode;
        do {
            currentCount++;
            generatedCode = String.format("%s%05d", prefix, currentCount);
        } while (purchaseOrderRepository.existsByPurchaseCodeIgnoreCase(generatedCode));
        return generatedCode;
    }

    @Cacheable(value = "purchaseOrders", key = "#purchaseId")
    public PurchaseOrderResponse getById(UUID purchaseId) {
        return purchaseOrderRepository.findById(purchaseId)
                .map(purchaseOrderMapper::toResponse)
                .orElseThrow(() -> new ApiException(ErrorCode.PURCHASE_ORDER_NOT_FOUND));
    }

    @Cacheable(value = "purchaseOrders", key = "{'all', #page, #limit}")
    public PageResult<PurchaseOrderResponse> getAll(Integer page, Integer limit) {
        int pageNumber = page == null || page < 1 ? 0 : page - 1;
        int pageSize = limit == null || limit <= 0 ? 10 : limit;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("purchaseDate").descending());

        Page<PurchaseOrderEntity> purchaseOrderPage = purchaseOrderRepository.findAll(pageable);
        return buildPageResult(purchaseOrderPage);
    }

    @Cacheable(value = "purchaseOrders", key = "{#keyword, #startDate, #endDate, #supplierId, #stockId, #employeeId, #orderType, #page, #limit}")
    public PageResult<PurchaseOrderResponse> search(
            String keyword,
            OffsetDateTime startDate,
            OffsetDateTime endDate,
            UUID supplierId,
            UUID stockId,
            UUID employeeId,
            String orderType,
            Integer page,
            Integer limit
    ) {
        int pageNumber = page == null || page < 1 ? 0 : page - 1;
        int pageSize = limit == null || limit <= 0 ? 10 : limit;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("purchaseDate").descending());

        Page<PurchaseOrderEntity> purchaseOrderPage = purchaseOrderRepository.searchPurchaseOrders(keyword, startDate, endDate, supplierId, stockId, employeeId, orderType, pageable);
        return buildPageResult(purchaseOrderPage);
    }

    @CacheEvict(value = {"purchaseOrders", "reports"}, allEntries = true)
    public PurchaseOrderResponse update(UUID purchaseId, PurchaseOrderUpdateRequest request) {
        PurchaseOrderEntity entity = purchaseOrderRepository.findById(purchaseId)
                .orElseThrow(() -> new ApiException(ErrorCode.PURCHASE_ORDER_NOT_FOUND));

        Object oldOrderType = entity.getOrderType();
        BigDecimal oldDebtDelta = BigDecimal.ZERO;
        SupplierEntity oldSupplier = entity.getSupplier();
        StockEntity oldStock = entity.getStock();
        if ("Nhaphang".equals(oldOrderType)) {
            oldDebtDelta = getDebtDelta(entity);
        }

        purchaseOrderMapper.updateEntity(request, entity);
        entity.setEmployee(getEmployeeOrNull(request.getEmployeeId()));
        entity.setSupplier(getSupplierOrNull(request.getSupplierId()));
        entity.setStock(getStockOrNull(request.getStockId()));

        PurchaseOrderEntity saved = purchaseOrderRepository.save(entity);

        // Revert old debt
        if ("Nhaphang".equals(oldOrderType) && oldSupplier != null) {
            oldSupplier.setDebt(oldSupplier.getDebt() != null ? oldSupplier.getDebt().subtract(oldDebtDelta) : oldDebtDelta.negate());
            supplierRepository.save(oldSupplier);
        }

        // Add new debt
        if ("Nhaphang".equals(saved.getOrderType()) && saved.getSupplier() != null) {
            SupplierEntity newSupplier = saved.getSupplier();
            BigDecimal newDebtDelta = getDebtDelta(saved);
            newSupplier.setDebt(newSupplier.getDebt() != null ? newSupplier.getDebt().add(newDebtDelta) : newDebtDelta);
            supplierRepository.save(newSupplier);
        }

        if (!"Nhaphang".equals(oldOrderType) && "Nhaphang".equals(saved.getOrderType())) {
            updateInventoryForOrder(saved);
        } else if ("Nhaphang".equals(oldOrderType) && !"Nhaphang".equals(saved.getOrderType())) {
            revertInventoryForOrder(saved);
        } else if ("Nhaphang".equals(oldOrderType) && "Nhaphang".equals(saved.getOrderType())) {
            StockEntity newStock = saved.getStock();
            if (oldStock != null && newStock != null && !oldStock.getStockId().equals(newStock.getStockId())) {
                java.util.List<com.example.GasTuanDat.purchase.entities.PurchaseDetailEntity> details = purchaseDetailRepository.findByPurchasePurchaseId(saved.getPurchaseId());
                for (com.example.GasTuanDat.purchase.entities.PurchaseDetailEntity detail : details) {
                    if (detail.getProduct() != null) {
                        java.util.Optional<InventoryEntity> oldInvOpt = inventoryRepository.findByStockStockIdAndProductProductId(oldStock.getStockId(), detail.getProduct().getProductId());
                        if (oldInvOpt.isPresent()) {
                            InventoryEntity oldInv = oldInvOpt.get();
                            oldInv.setQuantity(oldInv.getQuantity() - detail.getQuantity());
                            inventoryRepository.save(oldInv);
                        }
                        java.util.Optional<InventoryEntity> newInvOpt = inventoryRepository.findByStockStockIdAndProductProductId(newStock.getStockId(), detail.getProduct().getProductId());
                        if (newInvOpt.isPresent()) {
                            InventoryEntity newInv = newInvOpt.get();
                            newInv.setQuantity(newInv.getQuantity() + detail.getQuantity());
                            inventoryRepository.save(newInv);
                        } else {
                            InventoryEntity newInv = InventoryEntity.builder()
                                    .stock(newStock)
                                    .product(detail.getProduct())
                                    .quantity(detail.getQuantity())
                                    .build();
                            inventoryRepository.save(newInv);
                        }
                    }
                }
            }
        }

        return purchaseOrderMapper.toResponse(saved);
    }

    private BigDecimal getDebtDelta(PurchaseOrderEntity order) {
        BigDecimal total = order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO;
        BigDecimal discount = order.getDiscountAmount() != null ? order.getDiscountAmount() : BigDecimal.ZERO;
        BigDecimal paid = order.getPaidAmount() != null ? order.getPaidAmount() : BigDecimal.ZERO;
        return total.subtract(discount).subtract(paid);
    }

    private void updateInventoryForOrder(PurchaseOrderEntity order) {
        if (order.getStock() != null) {
            java.util.List<com.example.GasTuanDat.purchase.entities.PurchaseDetailEntity> details = purchaseDetailRepository.findByPurchasePurchaseId(order.getPurchaseId());
            for (com.example.GasTuanDat.purchase.entities.PurchaseDetailEntity detail : details) {
                if (detail.getProduct() != null) {
                    java.util.Optional<InventoryEntity> inventoryOpt = inventoryRepository
                            .findByStockStockIdAndProductProductId(order.getStock().getStockId(), detail.getProduct().getProductId());
                    if (inventoryOpt.isPresent()) {
                        InventoryEntity inventory = inventoryOpt.get();
                        inventory.setQuantity(inventory.getQuantity() + detail.getQuantity());
                        inventoryRepository.save(inventory);
                    } else {
                        InventoryEntity newInventory = InventoryEntity.builder()
                                .stock(order.getStock())
                                .product(detail.getProduct())
                                .quantity(detail.getQuantity())
                                .build();
                        inventoryRepository.save(newInventory);
                    }
                }
            }
        }
    }

    private void revertInventoryForOrder(PurchaseOrderEntity order) {
        if (order.getStock() != null) {
            java.util.List<com.example.GasTuanDat.purchase.entities.PurchaseDetailEntity> details = purchaseDetailRepository.findByPurchasePurchaseId(order.getPurchaseId());
            for (com.example.GasTuanDat.purchase.entities.PurchaseDetailEntity detail : details) {
                if (detail.getProduct() != null) {
                    java.util.Optional<InventoryEntity> inventoryOpt = inventoryRepository
                            .findByStockStockIdAndProductProductId(order.getStock().getStockId(), detail.getProduct().getProductId());
                    if (inventoryOpt.isPresent()) {
                        InventoryEntity inventory = inventoryOpt.get();
                        inventory.setQuantity(inventory.getQuantity() - detail.getQuantity());
                        inventoryRepository.save(inventory);
                    }
                }
            }
        }
    }

    @org.springframework.transaction.annotation.Transactional
    @CacheEvict(value = {"purchaseOrders", "reports"}, allEntries = true)
    public void delete(UUID purchaseId) {
        PurchaseOrderEntity entity = purchaseOrderRepository.findById(purchaseId)
                .orElseThrow(() -> new ApiException(ErrorCode.PURCHASE_ORDER_NOT_FOUND));

        if ("Nhaphang".equals(entity.getOrderType())) {
            revertInventoryForOrder(entity);
            if (entity.getSupplier() != null) {
                BigDecimal debtDelta = getDebtDelta(entity);
                SupplierEntity supplier = entity.getSupplier();
                supplier.setDebt(supplier.getDebt() != null ? supplier.getDebt().subtract(debtDelta) : debtDelta.negate());
                supplierRepository.save(supplier);
            }
        }

        purchaseDetailRepository.deleteByPurchase_PurchaseId(purchaseId);
        purchaseOrderRepository.delete(entity);
    }

    private EmployeeEntity getEmployeeOrNull(UUID employeeId) {
        if (employeeId == null) {
            return null;
        }
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ApiException(ErrorCode.EMPLOYEE_NOT_FOUND));
    }

    private SupplierEntity getSupplierOrNull(UUID supplierId) {
        if (supplierId == null) {
            return null;
        }
        return supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ApiException(ErrorCode.SUPPLIER_NOT_FOUND));
    }

    private StockEntity getStockOrNull(UUID stockId) {
        if (stockId == null) {
            return null;
        }
        return stockRepository.findById(stockId)
                .orElseThrow(() -> new ApiException(ErrorCode.STOCK_NOT_FOUND));
    }

    private PageResult<PurchaseOrderResponse> buildPageResult(Page<PurchaseOrderEntity> pageResult) {
        return PageResult.<PurchaseOrderResponse>builder()
                .content(pageResult.getContent().stream().map(purchaseOrderMapper::toResponse).toList())
                .page(pageResult.getNumber() + 1)
                .size(pageResult.getSize())
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .build();
    }
}
