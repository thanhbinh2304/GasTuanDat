package com.example.GasTuanDat.product.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.GasTuanDat.product.dtos.ProductCreateRequest;
import com.example.GasTuanDat.product.dtos.ProductResponse;
import com.example.GasTuanDat.product.dtos.ProductUpdateRequest;
import com.example.GasTuanDat.product.dtos.ProductStockDto;
import com.example.GasTuanDat.product.dtos.ProductPriceDto;
import com.example.GasTuanDat.product.dtos.ProductAttributeDto;
import com.example.GasTuanDat.product.entities.ProductEntity;
import com.example.GasTuanDat.stock.InventoryRepository;
import com.example.GasTuanDat.productPrice.ProductPriceRepository;
import com.example.GasTuanDat.productAttribute.ProductAttributeRepository;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductPriceRepository productPriceRepository;

    @Autowired
    private ProductAttributeRepository productAttributeRepository;

    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "unit", ignore = true) // Will be mapped manually in service
    public abstract ProductEntity toEntity(ProductCreateRequest request);

    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "categoryId", source = "category.categoryId")
    @Mapping(target = "categoryName", source = "category.categoryName")
    @Mapping(target = "unit", ignore = true) // Mapped manually below
    @Mapping(target = "warehouses", ignore = true)
    @Mapping(target = "priceTiers", ignore = true)
    @Mapping(target = "attributesList", ignore = true)
    public abstract ProductResponse toResponseBase(ProductEntity entity);

    public ProductResponse toResponse(ProductEntity entity) {
        if (entity == null) {
            return null;
        }
        ProductResponse response = toResponseBase(entity);
        
        // Map unit from Short to human-readable string
        String unitStr = "";
        if (entity.getUnit() != null) {
            short val = entity.getUnit();
            if (val == 1) unitStr = "Bộ";
            else if (val == 2) unitStr = "Cái";
            else if (val == 3) unitStr = "Bình";
            else if (val == 4) unitStr = "Bao";
            else unitStr = String.valueOf(val);
        }
        response.setUnit(unitStr);

        if (entity.getInventoryList() != null) {
            List<ProductStockDto> warehouses = entity.getInventoryList().stream()
                .map(inv -> ProductStockDto.builder()
                    .stockId(inv.getStock().getStockId())
                    .name(inv.getStock().getName())
                    .quantity(inv.getQuantity())
                    .build())
                .toList();
            response.setWarehouses(warehouses);
        }

        if (entity.getPriceTiers() != null) {
            List<ProductPriceDto> priceTiers = entity.getPriceTiers().stream()
                .map(pp -> ProductPriceDto.builder()
                    .priceListId(pp.getPriceList().getPriceListId())
                    .priceListName(pp.getPriceList().getPriceListName())
                    .price(pp.getSellingPrice())
                    .build())
                .toList();
            response.setPriceTiers(priceTiers);
        }

        if (entity.getAttributesList() != null) {
            List<ProductAttributeDto> attributesList = entity.getAttributesList().stream()
                .map(pa -> ProductAttributeDto.builder()
                    .attributeId(pa.getAttribute().getAttributeId())
                    .attributeName(pa.getAttribute().getAttributeName())
                    .value(pa.getAttributeValue())
                    .build())
                .toList();
            response.setAttributesList(attributesList);
        }
        
        return response;
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "productCode", source = "productCode")
    @Mapping(target = "productName", source = "productName")
    @Mapping(target = "cost", source = "cost")
    @Mapping(target = "note", source = "note")
    // Unit is handled manually in service update
    public abstract void updateEntity(ProductUpdateRequest request, @MappingTarget ProductEntity entity);
}
