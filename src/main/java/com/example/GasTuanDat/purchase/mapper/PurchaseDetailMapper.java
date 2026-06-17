package com.example.GasTuanDat.purchase.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.GasTuanDat.purchase.dtos.PurchaseDetailCreateRequest;
import com.example.GasTuanDat.purchase.dtos.PurchaseDetailResponse;
import com.example.GasTuanDat.purchase.dtos.PurchaseDetailUpdateRequest;
import com.example.GasTuanDat.purchase.entities.PurchaseDetailEntity;
import com.example.GasTuanDat.product.entities.ProductEntity;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.GasTuanDat.productAttribute.ProductAttributeRepository;
import com.example.GasTuanDat.productAttribute.entities.ProductAttributeEntity;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class PurchaseDetailMapper {

    @Autowired
    private ProductAttributeRepository productAttributeRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "purchase", ignore = true)
    @Mapping(target = "product", ignore = true)
    public abstract PurchaseDetailEntity toEntity(PurchaseDetailCreateRequest request);

    @Mapping(target = "purchaseId", expression = "java(entity.getPurchase() != null ? entity.getPurchase().getPurchaseId() : null)")
    @Mapping(target = "productId", expression = "java(entity.getProduct() != null ? entity.getProduct().getProductId() : null)")
    @Mapping(target = "productCode", expression = "java(entity.getProduct() != null ? entity.getProduct().getProductCode() : null)")
    @Mapping(target = "productName", expression = "java(mapProductName(entity.getProduct()))")
    @Mapping(target = "unit", expression = "java(mapUnit(entity.getProduct()))")
    public abstract PurchaseDetailResponse toResponse(PurchaseDetailEntity entity);

    public String mapUnit(ProductEntity product) {
        if (product == null || product.getUnit() == null) return null;
        short val = product.getUnit();
        if (val == 1) return "Bộ";
        if (val == 2) return "Cái";
        if (val == 3) return "Bình";
        if (val == 4) return "Bao";
        return String.valueOf(val);
    }

    public String mapProductName(ProductEntity product) {
        if (product == null) return null;
        String name = product.getProductName();
        if (product.getProductId() != null) {
            List<ProductAttributeEntity> attrs = productAttributeRepository.findByProductProductId(product.getProductId());
            if (attrs != null && !attrs.isEmpty()) {
                String attrsStr = attrs.stream()
                        .map(a -> a.getAttributeValue())
                        .filter(v -> v != null && !v.isEmpty())
                        .collect(java.util.stream.Collectors.joining(" - "));
                if (!attrsStr.isEmpty()) {
                    name = name + " - " + attrsStr;
                }
            }
        }
        return name;
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "purchasePrice", source = "purchasePrice")
    @Mapping(target = "total", source = "total")
    public abstract void updateEntity(PurchaseDetailUpdateRequest request, @MappingTarget PurchaseDetailEntity entity);
}
