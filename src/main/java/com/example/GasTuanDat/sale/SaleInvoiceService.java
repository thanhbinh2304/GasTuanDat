package com.example.GasTuanDat.sale;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

import com.example.GasTuanDat.common.exception.ApiException;
import com.example.GasTuanDat.common.exception.ErrorCode;
import com.example.GasTuanDat.common.response.PageResult;
import com.example.GasTuanDat.customer.CustomerRepository;
import com.example.GasTuanDat.customer.entities.CustomerEntity;
import com.example.GasTuanDat.employee.EmployeeRepository;
import com.example.GasTuanDat.employee.entities.EmployeeEntity;
import com.example.GasTuanDat.gasBook.GasBookRepository;
import com.example.GasTuanDat.gasBook.entities.GasBookEntity;
import com.example.GasTuanDat.product.ProductRepository;
import com.example.GasTuanDat.product.entities.ProductEntity;
import com.example.GasTuanDat.sale.dtos.SaleInvoiceCreateRequest;
import com.example.GasTuanDat.sale.dtos.SaleInvoiceDetailCreateRequest;
import com.example.GasTuanDat.sale.dtos.SaleInvoiceDetailResponse;
import com.example.GasTuanDat.sale.dtos.SaleInvoiceDetailUpdateRequest;
import com.example.GasTuanDat.sale.dtos.SaleInvoiceResponse;
import com.example.GasTuanDat.sale.dtos.SaleInvoiceUpdateRequest;
import com.example.GasTuanDat.sale.entities.SaleInvoiceDetailEntity;
import com.example.GasTuanDat.sale.entities.SaleInvoiceEntity;
import com.example.GasTuanDat.sale.mapper.SaleInvoiceDetailMapper;
import com.example.GasTuanDat.sale.mapper.SaleInvoiceMapper;
import com.example.GasTuanDat.stock.InventoryRepository;
import com.example.GasTuanDat.stock.StockRepository;
import com.example.GasTuanDat.stock.entities.InventoryEntity;
import com.example.GasTuanDat.stock.entities.StockEntity;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleInvoiceService {

    private final SaleInvoiceRepository saleInvoiceRepository;
    private final SaleInvoiceDetailRepository saleInvoiceDetailRepository;
    private final SaleInvoiceMapper saleInvoiceMapper;
    private final SaleInvoiceDetailMapper saleInvoiceDetailMapper;

    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final StockRepository stockRepository;
    private final GasBookRepository gasBookRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    @Transactional
    @CacheEvict(value = {"saleInvoices", "reports"}, allEntries = true)
    public SaleInvoiceResponse create(SaleInvoiceCreateRequest request) {
        SaleInvoiceEntity entity = saleInvoiceMapper.toEntity(request);
        
        if (entity.getInvoiceDate() == null) {
            entity.setInvoiceDate(OffsetDateTime.now());
        }

        if (entity.getInvoiceCode() == null || entity.getInvoiceCode().trim().isEmpty()) {
            entity.setInvoiceCode(generateInvoiceCode());
        }
        
        if (request.getOrderType() == null) {
            entity.setOrderType("Dathang");
        } else {
            entity.setOrderType(request.getOrderType());
        }

        entity.setEmployee(getEmployeeOrNull(request.getEmployeeId()));
        entity.setCustomer(getCustomerOrNull(request.getCustomerId()));
        entity.setStock(getStockOrNull(request.getStockId()));
        entity.setGasBook(getGasBookOrNull(request.getGasBookId()));

        SaleInvoiceEntity saved = saleInvoiceRepository.save(entity);

        if (request.getDetails() != null && !request.getDetails().isEmpty()) {
            for (SaleInvoiceDetailCreateRequest detailReq : request.getDetails()) {
                SaleInvoiceDetailEntity detail = saleInvoiceDetailMapper.toEntity(detailReq);
                detail.setInvoice(saved);
                ProductEntity product = productRepository.findById(detailReq.getProductId())
                        .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
                detail.setProduct(product);
                saleInvoiceDetailRepository.save(detail);

                // Update inventory if it's an actual sale, NOT just an order
                if (saved.getStock() != null && "Xuathang".equals(saved.getOrderType())) {
                    decreaseInventory(saved.getStock(), product, detailReq.getQuantity() != null ? detailReq.getQuantity().intValue() : 0);
                }
            }
        }

        if ("Xuathang".equals(saved.getOrderType())) {
            BigDecimal debtDelta = getDebtDelta(saved);
            if (saved.getCustomer() != null) {
                CustomerEntity customer = saved.getCustomer();
                customer.setDebt(customer.getDebt() != null ? customer.getDebt().add(debtDelta) : debtDelta);
                customerRepository.save(customer);
            }
            if (saved.getGasBook() != null) {
                GasBookEntity gasBook = saved.getGasBook();
                gasBook.setDebt(gasBook.getDebt() != null ? gasBook.getDebt().add(debtDelta) : debtDelta);
                gasBookRepository.save(gasBook);
                
                updateGasBookPoints(gasBook);
            }
        }

        return mapToResponseWithDetails(saved);
    }

    private BigDecimal getDebtDelta(SaleInvoiceEntity invoice) {
        BigDecimal total = invoice.getTotalAmount() != null ? invoice.getTotalAmount() : BigDecimal.ZERO;
        BigDecimal discount = invoice.getDiscountAmount() != null ? invoice.getDiscountAmount() : BigDecimal.ZERO;
        BigDecimal paid = invoice.getPaidAmount() != null ? invoice.getPaidAmount() : BigDecimal.ZERO;
        return total.subtract(discount).subtract(paid);
    }

    @Transactional
    @CacheEvict(value = {"saleInvoices", "reports"}, allEntries = true)
    public SaleInvoiceResponse update(UUID invoiceId, SaleInvoiceUpdateRequest request) {
        SaleInvoiceEntity entity = saleInvoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ApiException(ErrorCode.SALE_INVOICE_NOT_FOUND));

        Object oldOrderType = entity.getOrderType();
        BigDecimal oldDebtDelta = BigDecimal.ZERO;
        CustomerEntity oldCustomer = entity.getCustomer();
        GasBookEntity oldGasBook = entity.getGasBook();
        
        if ("Xuathang".equals(oldOrderType)) {
            oldDebtDelta = getDebtDelta(entity);
            revertInventory(entity);
        }

        saleInvoiceMapper.updateEntity(request, entity);

        entity.setEmployee(getEmployeeOrNull(request.getEmployeeId()));
        entity.setCustomer(getCustomerOrNull(request.getCustomerId()));
        entity.setStock(getStockOrNull(request.getStockId()));
        entity.setGasBook(getGasBookOrNull(request.getGasBookId()));

        SaleInvoiceEntity saved = saleInvoiceRepository.save(entity);

        // Revert old debt
        if ("Xuathang".equals(oldOrderType)) {
            if (oldCustomer != null) {
                oldCustomer.setDebt(oldCustomer.getDebt() != null ? oldCustomer.getDebt().subtract(oldDebtDelta) : oldDebtDelta.negate());
                customerRepository.save(oldCustomer);
            }
            if (oldGasBook != null) {
                oldGasBook.setDebt(oldGasBook.getDebt() != null ? oldGasBook.getDebt().subtract(oldDebtDelta) : oldDebtDelta.negate());
                gasBookRepository.save(oldGasBook);
            }
        }

        // Add new debt
        if ("Xuathang".equals(saved.getOrderType())) {
            BigDecimal newDebtDelta = getDebtDelta(saved);
            if (saved.getCustomer() != null) {
                CustomerEntity customer = saved.getCustomer();
                customer.setDebt(customer.getDebt() != null ? customer.getDebt().add(newDebtDelta) : newDebtDelta);
                customerRepository.save(customer);
            }
            if (saved.getGasBook() != null) {
                GasBookEntity gasBook = saved.getGasBook();
                gasBook.setDebt(gasBook.getDebt() != null ? gasBook.getDebt().add(newDebtDelta) : newDebtDelta);
                gasBookRepository.save(gasBook);
            }
        }

        saleInvoiceDetailRepository.deleteByInvoice_InvoiceId(saved.getInvoiceId());
        saleInvoiceDetailRepository.flush();

        if (request.getDetails() != null && !request.getDetails().isEmpty()) {
            for (SaleInvoiceDetailUpdateRequest detailReq : request.getDetails()) {
                SaleInvoiceDetailEntity detail = new SaleInvoiceDetailEntity();
                saleInvoiceDetailMapper.updateEntity(detailReq, detail);
                detail.setInvoice(saved);
                ProductEntity product = productRepository.findById(detailReq.getProductId())
                        .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
                detail.setProduct(product);
                saleInvoiceDetailRepository.save(detail);

                if (saved.getStock() != null && "Xuathang".equals(saved.getOrderType())) {
                    decreaseInventory(saved.getStock(), product, detailReq.getQuantity() != null ? detailReq.getQuantity().intValue() : 0);
                }
            }
        }
        saleInvoiceDetailRepository.flush();

        if (oldGasBook != null) {
            updateGasBookPoints(oldGasBook);
        }
        if (saved.getGasBook() != null && (oldGasBook == null || !oldGasBook.getGasBookId().equals(saved.getGasBook().getGasBookId()))) {
            updateGasBookPoints(saved.getGasBook());
        }

        return mapToResponseWithDetails(saved);
    }

    @Cacheable(value = "saleInvoices", key = "#invoiceId")
    public SaleInvoiceResponse getById(UUID invoiceId) {
        SaleInvoiceEntity entity = saleInvoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ApiException(ErrorCode.SALE_INVOICE_NOT_FOUND));
        return mapToResponseWithDetails(entity);
    }

    @Cacheable(value = "saleInvoices", key = "{#keyword, #startDate, #endDate, #customerId, #stockId, #employeeId, #orderType, #customerGroupId, #page, #limit}")
    public PageResult<SaleInvoiceResponse> search(
            String keyword,
            OffsetDateTime startDate,
            OffsetDateTime endDate,
            UUID customerId,
            UUID stockId,
            UUID employeeId,
            String orderType,
            UUID customerGroupId,
            Integer page,
            Integer limit) {
        int pageNumber = page == null || page < 1 ? 0 : page - 1;
        int pageSize = limit == null || limit <= 0 ? 10 : limit;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("invoiceDate").descending());

        Page<SaleInvoiceEntity> pagedResult = saleInvoiceRepository.searchSaleInvoices(
                keyword, startDate, endDate, customerId, stockId, employeeId, orderType, customerGroupId, pageable);

        return buildPageResult(pagedResult);
    }

    @Transactional
    @CacheEvict(value = {"saleInvoices", "reports"}, allEntries = true)
    public void delete(UUID invoiceId) {
        SaleInvoiceEntity entity = saleInvoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ApiException(ErrorCode.SALE_INVOICE_NOT_FOUND));

        if ("Xuathang".equals(entity.getOrderType())) {
            revertInventory(entity);
            BigDecimal debtDelta = getDebtDelta(entity);
            if (entity.getCustomer() != null) {
                CustomerEntity customer = entity.getCustomer();
                customer.setDebt(customer.getDebt() != null ? customer.getDebt().subtract(debtDelta) : debtDelta.negate());
                customerRepository.save(customer);
            }
            if (entity.getGasBook() != null) {
                GasBookEntity gasBook = entity.getGasBook();
                gasBook.setDebt(gasBook.getDebt() != null ? gasBook.getDebt().subtract(debtDelta) : debtDelta.negate());
                gasBookRepository.save(gasBook);
            }
        }

        saleInvoiceDetailRepository.deleteByInvoice_InvoiceId(invoiceId);
        saleInvoiceDetailRepository.flush();
        saleInvoiceRepository.delete(entity);
        saleInvoiceRepository.flush();

        if ("Xuathang".equals(entity.getOrderType()) && entity.getGasBook() != null) {
            updateGasBookPoints(entity.getGasBook());
        }
    }

    private void revertInventory(SaleInvoiceEntity invoice) {
        if (invoice.getStock() == null) return;
        List<SaleInvoiceDetailEntity> details = saleInvoiceDetailRepository.findByInvoice_InvoiceId(invoice.getInvoiceId());
        for (SaleInvoiceDetailEntity detail : details) {
            if (detail.getQuantity() != null) {
                increaseInventory(invoice.getStock(), detail.getProduct(), detail.getQuantity().intValue());
            }
        }
    }

    private void decreaseInventory(StockEntity stock, ProductEntity product, int quantity) {
        InventoryEntity inventory = inventoryRepository.findByStockStockIdAndProductProductId(stock.getStockId(), product.getProductId())
                .orElseGet(() -> {
                    InventoryEntity newInv = new InventoryEntity();
                    newInv.setStock(stock);
                    newInv.setProduct(product);
                    newInv.setQuantity(0);
                    return inventoryRepository.save(newInv);
                });
        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryRepository.save(inventory);
    }

    private void increaseInventory(StockEntity stock, ProductEntity product, int quantity) {
        InventoryEntity inventory = inventoryRepository.findByStockStockIdAndProductProductId(stock.getStockId(), product.getProductId())
                .orElseGet(() -> {
                    InventoryEntity newInv = new InventoryEntity();
                    newInv.setStock(stock);
                    newInv.setProduct(product);
                    newInv.setQuantity(0);
                    return inventoryRepository.save(newInv);
                });
        inventory.setQuantity(inventory.getQuantity() + quantity);
        inventoryRepository.save(inventory);
    }

    private String generateInvoiceCode() {
        long currentCount = saleInvoiceRepository.count();
        String prefix = "HDX";
        String generatedCode;
        do {
            currentCount++;
            generatedCode = String.format("%s%05d", prefix, currentCount);
        } while (saleInvoiceRepository.existsByInvoiceCodeIgnoreCase(generatedCode));
        return generatedCode;
    }

    private EmployeeEntity getEmployeeOrNull(UUID employeeId) {
        if (employeeId == null) return null;
        return employeeRepository.findById(employeeId).orElse(null);
    }

    private CustomerEntity getCustomerOrNull(UUID customerId) {
        if (customerId == null) return null;
        return customerRepository.findById(customerId).orElse(null);
    }

    private StockEntity getStockOrNull(UUID stockId) {
        if (stockId == null) return null;
        return stockRepository.findById(stockId).orElse(null);
    }

    private GasBookEntity getGasBookOrNull(UUID gasBookId) {
        if (gasBookId == null) return null;
        return gasBookRepository.findById(gasBookId).orElse(null);
    }

    private SaleInvoiceResponse mapToResponseWithDetails(SaleInvoiceEntity entity) {
        SaleInvoiceResponse response = saleInvoiceMapper.toResponse(entity);
        List<SaleInvoiceDetailEntity> details = saleInvoiceDetailRepository.findByInvoice_InvoiceId(entity.getInvoiceId());
        response.setDetails(details.stream().map(saleInvoiceDetailMapper::toResponse).collect(Collectors.toList()));
        return response;
    }

    private PageResult<SaleInvoiceResponse> buildPageResult(Page<SaleInvoiceEntity> pageResult) {
        return PageResult.<SaleInvoiceResponse>builder()
                .content(pageResult.getContent().stream().map(this::mapToResponseWithDetails).collect(Collectors.toList()))
                .page(pageResult.getNumber() + 1)
                .size(pageResult.getSize())
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .build();
    }

    private void updateGasBookPoints(GasBookEntity gasBook) {
        if (gasBook == null) return;
        Integer totalGasCylinders = saleInvoiceDetailRepository.countTotalGasCylindersForGasBook(gasBook.getGasBookId());
        if (totalGasCylinders == null) totalGasCylinders = 0;
        
        int points = totalGasCylinders % 57 == 0 ? (totalGasCylinders == 0 ? 0 : 57) : (totalGasCylinders % 57);
        int cycle = totalGasCylinders == 0 ? 0 : (totalGasCylinders - 1) / 57 + 1;
        
        gasBook.setPoints(points);
        gasBook.setCycle(cycle);
        gasBookRepository.save(gasBook);
    }
}
