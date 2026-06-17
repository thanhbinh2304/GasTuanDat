package com.example.GasTuanDat.sale.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.GasTuanDat.sale.dtos.SaleInvoiceDetailCreateRequest;
import com.example.GasTuanDat.sale.dtos.SaleInvoiceDetailResponse;
import com.example.GasTuanDat.sale.dtos.SaleInvoiceDetailUpdateRequest;
import com.example.GasTuanDat.sale.entities.SaleInvoiceDetailEntity;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.GasTuanDat.productAttribute.ProductAttributeRepository;
import com.example.GasTuanDat.productAttribute.entities.ProductAttributeEntity;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class SaleInvoiceDetailMapper {

    @Autowired
    private ProductAttributeRepository productAttributeRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "invoice", ignore = true)
    @Mapping(target = "product", ignore = true)
    public abstract SaleInvoiceDetailEntity toEntity(SaleInvoiceDetailCreateRequest request);

    @Mapping(target = "invoiceId", expression = "java(entity.getInvoice() != null ? entity.getInvoice().getInvoiceId() : null)")
    @Mapping(target = "productId", expression = "java(entity.getProduct() != null ? entity.getProduct().getProductId() : null)")
    @Mapping(target = "productCode", expression = "java(entity.getProduct() != null ? entity.getProduct().getProductCode() : null)")
    @Mapping(target = "productName", expression = "java(mapProductName(entity.getProduct()))")
    @Mapping(target = "unit", expression = "java(entity.getProduct() != null ? entity.getProduct().getUnit() : null)")
    public abstract SaleInvoiceDetailResponse toResponse(SaleInvoiceDetailEntity entity);

    public String mapProductName(com.example.GasTuanDat.product.entities.ProductEntity product) {
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
    @Mapping(target = "unitPrice", source = "unitPrice")
    @Mapping(target = "total", source = "total")
    public abstract void updateEntity(SaleInvoiceDetailUpdateRequest request, @MappingTarget SaleInvoiceDetailEntity entity);
}
