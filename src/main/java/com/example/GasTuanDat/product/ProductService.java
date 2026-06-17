package com.example.GasTuanDat.product;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.GasTuanDat.common.exception.ApiException;
import com.example.GasTuanDat.common.exception.ErrorCode;
import com.example.GasTuanDat.product.dtos.ProductCreateRequest;
import com.example.GasTuanDat.product.dtos.ProductUpdateRequest;
import com.example.GasTuanDat.product.dtos.ProductStockDto;
import com.example.GasTuanDat.product.dtos.ProductPriceDto;
import com.example.GasTuanDat.product.dtos.ProductAttributeDto;
import com.example.GasTuanDat.product.dtos.ProductResponse;
import com.example.GasTuanDat.product.entities.ProductEntity;
import com.example.GasTuanDat.productCategory.ProductCategoryRepository;
import com.example.GasTuanDat.productCategory.entities.ProductCategoryEntity;

import com.example.GasTuanDat.stock.StockRepository;
import com.example.GasTuanDat.stock.InventoryRepository;
import com.example.GasTuanDat.productPrice.ProductPriceRepository;
import com.example.GasTuanDat.productAttribute.ProductAttributeRepository;
import com.example.GasTuanDat.productPrice.PriceListRepository;
import com.example.GasTuanDat.productAttribute.AttributeRepository;

import com.example.GasTuanDat.stock.entities.StockEntity;
import com.example.GasTuanDat.stock.entities.InventoryEntity;
import com.example.GasTuanDat.productPrice.entities.ProductPriceEntity;
import com.example.GasTuanDat.productPrice.entities.PriceListEntity;
import com.example.GasTuanDat.productAttribute.entities.ProductAttributeEntity;
import com.example.GasTuanDat.productAttribute.entities.AttributeEntity;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    private final StockRepository stockRepository;
    private final InventoryRepository inventoryRepository;
    private final ProductPriceRepository productPriceRepository;
    private final ProductAttributeRepository productAttributeRepository;
    private final PriceListRepository priceListRepository;
    private final AttributeRepository attributeRepository;
    private final com.example.GasTuanDat.product.mapper.ProductMapper productMapper;

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public ProductEntity create(ProductCreateRequest request) {
        String name = request.getProductName().trim();
        validateProductName(name);
        validateCost(request.getCost());


        ProductCategoryEntity category = productCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_CATEGORY_NOT_FOUND));

        String code = request.getProductCode();
        if (code == null || code.trim().isEmpty()) {
            code = generateNextProductCode(category.getCategoryName());
        } else {
            code = code.trim();
            if (productRepository.existsByProductCodeIgnoreCase(code)) {
                throw new ApiException(ErrorCode.PRODUCT_CODE_ALREADY_EXISTS);
            }
        }

        // Map text unit string to Short for DB column
        Short unitVal = null;
        if (request.getUnit() != null && !request.getUnit().trim().isEmpty()) {
            String u = request.getUnit().trim().toLowerCase();
            if (u.contains("bộ")) unitVal = 1;
            else if (u.contains("cái") || u.contains("chiếc")) unitVal = 2;
            else if (u.contains("bình")) unitVal = 3;
            else if (u.contains("bao")) unitVal = 4;
            else {
                try {
                    unitVal = Short.parseShort(u);
                } catch (NumberFormatException e) {
                    unitVal = 0;
                }
            }
        }

        ProductEntity product = ProductEntity.builder()
                .productCode(code)
                .productName(name)
                .cost(request.getCost())
                .category(category)
                .unit(unitVal)
                .note(request.getNote())
                .build();

        ProductEntity saved = productRepository.save(product);

        // Save warehouses
        if (request.getWarehouses() != null) {
            for (ProductStockDto w : request.getWarehouses()) {
                if (w.getStockId() != null) {
                    StockEntity stock = stockRepository.findById(w.getStockId())
                        .orElseThrow(() -> new ApiException(ErrorCode.INVALID_INPUT));
                    InventoryEntity inv = InventoryEntity.builder()
                        .product(saved)
                        .stock(stock)
                        .quantity(w.getQuantity() != null ? w.getQuantity() : 0)
                        .build();
                    inventoryRepository.save(inv);
                }
            }
        }

        // Save price tiers
        if (request.getPriceTiers() != null) {
            for (ProductPriceDto p : request.getPriceTiers()) {
                if (p.getPriceListId() != null) {
                    PriceListEntity priceList = priceListRepository.findById(p.getPriceListId())
                        .orElseThrow(() -> new ApiException(ErrorCode.INVALID_INPUT));
                    ProductPriceEntity pp = ProductPriceEntity.builder()
                        .product(saved)
                        .priceList(priceList)
                        .sellingPrice(p.getPrice() != null ? p.getPrice() : BigDecimal.ZERO)
                        .build();
                    productPriceRepository.save(pp);
                }
            }
        }

        // Save attributes
        if (request.getAttributesList() != null) {
            for (ProductAttributeDto a : request.getAttributesList()) {
                if (a.getAttributeId() != null) {
                    AttributeEntity attr = attributeRepository.findById(a.getAttributeId())
                        .orElseThrow(() -> new ApiException(ErrorCode.INVALID_INPUT));
                    ProductAttributeEntity pa = ProductAttributeEntity.builder()
                        .product(saved)
                        .attribute(attr)
                        .attributeValue(a.getValue() != null ? a.getValue() : "")
                        .build();
                    productAttributeRepository.save(pa);
                }
            }
        }

        return saved;
    }

    private String generateNextProductCode(String categoryName) {
        String prefix = getCategoryAbbreviation(categoryName);
        if (prefix.isEmpty()) {
            prefix = "SP";
        }

        List<String> codes = productRepository.findProductCodesByPrefix(prefix);
        java.util.regex.Pattern pattern = java.util.regex.Pattern
                .compile("^" + java.util.regex.Pattern.quote(prefix) + "(\\d+)$");
        int maxNum = 0;

        for (String code : codes) {
            if (code != null) {
                java.util.regex.Matcher matcher = pattern.matcher(code);
                if (matcher.matches()) {
                    try {
                        int num = Integer.parseInt(matcher.group(1));
                        if (num > maxNum) {
                            maxNum = num;
                        }
                    } catch (NumberFormatException e) {
                        // ignore
                    }
                }
            }
        }

        int nextNum = maxNum + 1;
        String nextNumStr = nextNum < 10 ? "0" + nextNum : String.valueOf(nextNum);
        return prefix + nextNumStr;
    }

    private String getCategoryAbbreviation(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            return "";
        }
        String unsigned = removeVietnameseTones(categoryName);
        String[] words = unsigned.trim().split("\\s+");
        StringBuilder abbreviation = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                abbreviation.append(word.charAt(0));
            }
        }
        return abbreviation.toString().toUpperCase();
    }

    private String removeVietnameseTones(String str) {
        if (str == null) {
            return "";
        }
        String temp = java.text.Normalizer.normalize(str, java.text.Normalizer.Form.NFD);
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String unsigned = pattern.matcher(temp).replaceAll("");
        return unsigned.replace('đ', 'd').replace('Đ', 'D');
    }

    @Cacheable(value = "products", key = "'all'")
    public List<ProductEntity> getAll() {
        return productRepository.findAll();
    }

    @Cacheable(value = "products", key = "#productId")
    public ProductEntity getById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public ProductEntity update(UUID productId, ProductUpdateRequest request) {
        ProductEntity product = getById(productId);

        if (request.getProductName() != null) {
            String name = request.getProductName().trim();
            validateProductName(name);

            product.setProductName(name);
        }

        if (request.getCost() != null) {
            validateCost(request.getCost());
            product.setCost(request.getCost());
        }

        if (request.getCategoryId() != null) {
            ProductCategoryEntity category = productCategoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_CATEGORY_NOT_FOUND));
            product.setCategory(category);
        }

        if (request.getUnit() != null) {
            Short unitVal = null;
            if (!request.getUnit().trim().isEmpty()) {
                String u = request.getUnit().trim().toLowerCase();
                if (u.contains("bộ")) unitVal = 1;
                else if (u.contains("cái") || u.contains("chiếc")) unitVal = 2;
                else if (u.contains("bình")) unitVal = 3;
                else if (u.contains("bao")) unitVal = 4;
                else {
                    try {
                        unitVal = Short.parseShort(u);
                    } catch (NumberFormatException e) {
                        unitVal = 0;
                    }
                }
            }
            product.setUnit(unitVal);
        }

        if (request.getNote() != null) {
            product.setNote(request.getNote());
        }

        ProductEntity saved = productRepository.save(product);

        // Update warehouses without delete-and-recreate
        if (request.getWarehouses() != null) {
            java.util.List<InventoryEntity> currentInventories = product.getInventoryList();
            if (currentInventories == null) {
                currentInventories = new java.util.ArrayList<>();
                product.setInventoryList(currentInventories);
            }
            
            java.util.Map<UUID, Integer> requestStocks = new java.util.HashMap<>();
            for (ProductStockDto w : request.getWarehouses()) {
                if (w.getStockId() != null) {
                    requestStocks.put(w.getStockId(), w.getQuantity() != null ? w.getQuantity() : 0);
                }
            }

            // Remove absent
            currentInventories.removeIf(inv -> !requestStocks.containsKey(inv.getStock().getStockId()));

            // Update existing or Add new
            for (java.util.Map.Entry<UUID, Integer> entry : requestStocks.entrySet()) {
                UUID stockId = entry.getKey();
                Integer quantity = entry.getValue();

                java.util.Optional<InventoryEntity> existing = currentInventories.stream()
                        .filter(inv -> inv.getStock().getStockId().equals(stockId))
                        .findFirst();

                if (existing.isPresent()) {
                    existing.get().setQuantity(quantity);
                } else {
                    StockEntity stock = stockRepository.findById(stockId)
                        .orElseThrow(() -> new ApiException(ErrorCode.INVALID_INPUT));
                    InventoryEntity inv = InventoryEntity.builder()
                        .product(product)
                        .stock(stock)
                        .quantity(quantity)
                        .build();
                    currentInventories.add(inv);
                }
            }
        }

        // Update price tiers without delete-and-recreate
        if (request.getPriceTiers() != null) {
            java.util.List<ProductPriceEntity> currentPrices = product.getPriceTiers();
            if (currentPrices == null) {
                currentPrices = new java.util.ArrayList<>();
                product.setPriceTiers(currentPrices);
            }

            java.util.Map<UUID, BigDecimal> requestPrices = new java.util.HashMap<>();
            for (ProductPriceDto p : request.getPriceTiers()) {
                if (p.getPriceListId() != null) {
                    requestPrices.put(p.getPriceListId(), p.getPrice() != null ? p.getPrice() : BigDecimal.ZERO);
                }
            }

            currentPrices.removeIf(pp -> !requestPrices.containsKey(pp.getPriceList().getPriceListId()));

            for (java.util.Map.Entry<UUID, BigDecimal> entry : requestPrices.entrySet()) {
                UUID priceListId = entry.getKey();
                BigDecimal price = entry.getValue();

                java.util.Optional<ProductPriceEntity> existing = currentPrices.stream()
                        .filter(pp -> pp.getPriceList().getPriceListId().equals(priceListId))
                        .findFirst();

                if (existing.isPresent()) {
                    existing.get().setSellingPrice(price);
                } else {
                    PriceListEntity priceList = priceListRepository.findById(priceListId)
                        .orElseThrow(() -> new ApiException(ErrorCode.INVALID_INPUT));
                    ProductPriceEntity pp = ProductPriceEntity.builder()
                        .product(product)
                        .priceList(priceList)
                        .sellingPrice(price)
                        .build();
                    currentPrices.add(pp);
                }
            }
        }

        // Update attributes without delete-and-recreate
        if (request.getAttributesList() != null) {
            java.util.List<ProductAttributeEntity> currentAttributes = product.getAttributesList();
            if (currentAttributes == null) {
                currentAttributes = new java.util.ArrayList<>();
                product.setAttributesList(currentAttributes);
            }

            java.util.Map<UUID, String> requestAttributes = new java.util.HashMap<>();
            for (ProductAttributeDto a : request.getAttributesList()) {
                if (a.getAttributeId() != null) {
                    requestAttributes.put(a.getAttributeId(), a.getValue() != null ? a.getValue() : "");
                }
            }

            currentAttributes.removeIf(pa -> !requestAttributes.containsKey(pa.getAttribute().getAttributeId()));

            for (java.util.Map.Entry<UUID, String> entry : requestAttributes.entrySet()) {
                UUID attributeId = entry.getKey();
                String value = entry.getValue();

                java.util.Optional<ProductAttributeEntity> existing = currentAttributes.stream()
                        .filter(pa -> pa.getAttribute().getAttributeId().equals(attributeId))
                        .findFirst();

                if (existing.isPresent()) {
                    existing.get().setAttributeValue(value);
                } else {
                    AttributeEntity attr = attributeRepository.findById(attributeId)
                        .orElseThrow(() -> new ApiException(ErrorCode.INVALID_INPUT));
                    ProductAttributeEntity pa = ProductAttributeEntity.builder()
                        .product(product)
                        .attribute(attr)
                        .attributeValue(value)
                        .build();
                    currentAttributes.add(pa);
                }
            }
        }

        return productRepository.save(product);
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public void delete(UUID productId) {
        ProductEntity product = getById(productId);
        product.setDeleted(true);
        productRepository.save(product);
    }

    @Cacheable(value = "products", key = "{#keyword, #productCategory, #stock, #priceList, #productAttribute, #attributeValue, #page, #pageSize}", sync = true)
    public Page<ProductResponse> search(String keyword,
            String productCategory,
            String stock,
            String priceList,
            String productAttribute,
            String attributeValue,
            int page,
            int pageSize) {
        String normalizedKeyword = normalizeFilter(keyword);
        String normalizedProductCategory = normalizeFilter(productCategory);
        String normalizedStock = normalizeFilter(stock);
        String normalizedPriceList = normalizeFilter(priceList);
        String normalizedProductAttribute = normalizeFilter(productAttribute);
        String normalizedAttributeValue = normalizeFilter(attributeValue);

        UUID stockId = tryParseUuid(normalizedStock);
        String stockName = stockId == null ? normalizedStock : null;

        UUID priceListId = tryParseUuid(normalizedPriceList);
        String priceListName = priceListId == null ? normalizedPriceList : null;

        UUID productAttributeId = tryParseUuid(normalizedProductAttribute);
        String productAttributeName = productAttributeId == null ? normalizedProductAttribute : null;

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<ProductEntity> pageResult = productRepository.searchByFilters(
                normalizedKeyword,
                normalizedProductCategory,
                stockId,
                stockName,
                priceListId,
                priceListName,
                productAttributeId,
                productAttributeName,
                normalizedAttributeValue,
                pageable);
        return pageResult.map(productMapper::toResponse);
    }

    @Cacheable(value = "products", key = "{#productName, #page, #limit}", sync = true)
    public Page<ProductResponse> search(String productName, int page, int limit) {
        return search(productName, null, null, null, null, null, page, limit);
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

    private void validateProductName(String name) {
        if (name == null || name.isBlank()) {
            throw new ApiException(ErrorCode.INVALID_INPUT);
        }
    }

    private void validateCost(BigDecimal cost) {
        if (cost == null || cost.compareTo(BigDecimal.ZERO) < 0) {
            throw new ApiException(ErrorCode.INVALID_INPUT);
        }
    }
}
