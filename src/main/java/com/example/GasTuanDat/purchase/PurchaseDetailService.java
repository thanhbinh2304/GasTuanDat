package com.example.GasTuanDat.purchase;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.GasTuanDat.common.exception.ApiException;
import com.example.GasTuanDat.common.exception.ErrorCode;
import com.example.GasTuanDat.product.ProductRepository;
import com.example.GasTuanDat.product.entities.ProductEntity;
import com.example.GasTuanDat.purchase.dtos.PurchaseDetailCreateRequest;
import com.example.GasTuanDat.purchase.dtos.PurchaseDetailResponse;
import com.example.GasTuanDat.purchase.dtos.PurchaseDetailUpdateRequest;
import com.example.GasTuanDat.purchase.entities.PurchaseDetailEntity;
import com.example.GasTuanDat.purchase.entities.PurchaseOrderEntity;
import com.example.GasTuanDat.purchase.mapper.PurchaseDetailMapper;

import com.example.GasTuanDat.stock.InventoryRepository;
import com.example.GasTuanDat.stock.entities.InventoryEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseDetailService {
    private final PurchaseDetailRepository purchaseDetailRepository;
    private final PurchaseDetailMapper purchaseDetailMapper;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public PurchaseDetailResponse create(PurchaseDetailCreateRequest request) {
        PurchaseDetailEntity entity = purchaseDetailMapper.toEntity(request);
        PurchaseOrderEntity order = getPurchaseOrderOrNull(request.getPurchaseId());
        entity.setPurchase(order);
        ProductEntity product = getProductOrNull(request.getProductId());
        entity.setProduct(product);
        
        PurchaseDetailEntity saved = purchaseDetailRepository.save(entity);

        if (order != null && "Nhaphang".equals(order.getOrderType())) {
            updateInventory(order, product, request.getQuantity());
        }

        return purchaseDetailMapper.toResponse(saved);
    }

    private void updateInventory(PurchaseOrderEntity order, ProductEntity product, int quantity) {
        if (order.getStock() != null && product != null) {
            java.util.Optional<InventoryEntity> inventoryOpt = inventoryRepository
                    .findByStockStockIdAndProductProductId(order.getStock().getStockId(), product.getProductId());
            if (inventoryOpt.isPresent()) {
                InventoryEntity inventory = inventoryOpt.get();
                inventory.setQuantity(inventory.getQuantity() + quantity);
                inventoryRepository.save(inventory);
            } else {
                InventoryEntity newInventory = InventoryEntity.builder()
                        .stock(order.getStock())
                        .product(product)
                        .quantity(quantity)
                        .build();
                inventoryRepository.save(newInventory);
            }
        }
    }

    public PurchaseDetailResponse getById(UUID id) {
        return purchaseDetailRepository.findById(id)
                .map(purchaseDetailMapper::toResponse)
                .orElseThrow(() -> new ApiException(ErrorCode.PURCHASE_DETAIL_NOT_FOUND));
    }

    public java.util.List<PurchaseDetailResponse> getAll() {
        return purchaseDetailRepository.findAll().stream()
                .map(purchaseDetailMapper::toResponse)
                .toList();
    }

    public java.util.List<PurchaseDetailResponse> getByPurchaseId(UUID purchaseId) {
        return purchaseDetailRepository.findByPurchasePurchaseId(purchaseId).stream()
                .map(purchaseDetailMapper::toResponse)
                .toList();
    }

    public PurchaseDetailResponse update(UUID id, PurchaseDetailUpdateRequest request) {
        PurchaseDetailEntity entity = purchaseDetailRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.PURCHASE_DETAIL_NOT_FOUND));

        purchaseDetailMapper.updateEntity(request, entity);
        entity.setPurchase(getPurchaseOrderOrNull(request.getPurchaseId()));
        entity.setProduct(getProductOrNull(request.getProductId()));

        return purchaseDetailMapper.toResponse(purchaseDetailRepository.save(entity));
    }

    public void delete(UUID id) {
        PurchaseDetailEntity entity = purchaseDetailRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.PURCHASE_DETAIL_NOT_FOUND));
                
        PurchaseOrderEntity order = entity.getPurchase();
        if (order != null && "Nhaphang".equals(order.getOrderType())) {
            updateInventory(order, entity.getProduct(), -entity.getQuantity());
        }
        
        purchaseDetailRepository.delete(entity);
    }

    private PurchaseOrderEntity getPurchaseOrderOrNull(UUID purchaseId) {
        if (purchaseId == null) {
            return null;
        }
        return purchaseOrderRepository.findById(purchaseId)
                .orElseThrow(() -> new ApiException(ErrorCode.PURCHASE_ORDER_NOT_FOUND));
    }

    private ProductEntity getProductOrNull(UUID productId) {
        if (productId == null) {
            return null;
        }
        return productRepository.findById(productId)
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
    }
}
