package com.example.GasTuanDat.gasBook;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.GasTuanDat.common.exception.ApiException;
import com.example.GasTuanDat.common.exception.ErrorCode;
import com.example.GasTuanDat.common.response.PageResult;
import com.example.GasTuanDat.customer.CustomerGroupRepository;
import com.example.GasTuanDat.customer.entities.CustomerGroupEntity;
import com.example.GasTuanDat.gasBook.dtos.GasBookCreateRequest;
import com.example.GasTuanDat.gasBook.dtos.GasBookResponse;
import com.example.GasTuanDat.gasBook.dtos.GasBookUpdateRequest;
import com.example.GasTuanDat.gasBook.entities.GasBookEntity;
import com.example.GasTuanDat.gasBook.mapper.GasBookMapper;
import com.example.GasTuanDat.ward.WardRepository;
import com.example.GasTuanDat.ward.entities.WardEntity;
import com.example.GasTuanDat.sale.SaleInvoiceRepository;
import com.example.GasTuanDat.sale.SaleInvoiceDetailRepository;
import com.example.GasTuanDat.gasBook.dtos.GasBookHistoryResponse;
import com.example.GasTuanDat.gasBook.dtos.GasBookDetailedHistoryResponse;
import com.example.GasTuanDat.sale.entities.SaleInvoiceEntity;
import com.example.GasTuanDat.sale.entities.SaleInvoiceDetailEntity;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GasBookService {
    private final GasBookRepository gasBookRepository;
    private final GasBookMapper gasBookMapper;
    private final WardRepository wardRepository;
    private final CustomerGroupRepository customerGroupRepository;
    private final SaleInvoiceRepository saleInvoiceRepository;
    private final SaleInvoiceDetailRepository saleInvoiceDetailRepository;

    public GasBookResponse create(GasBookCreateRequest request) {
        GasBookEntity entity = gasBookMapper.toEntity(request);
        
        if (entity.getGasBookCode() == null || entity.getGasBookCode().trim().isEmpty()) {
            entity.setGasBookCode(generateGasBookCode());
        } else {
            if (gasBookRepository.existsByGasBookCodeIgnoreCase(entity.getGasBookCode())) {
                throw new ApiException(ErrorCode.CUSTOMER_CODE_ALREADY_EXISTS); // Re-using customer code already exists
            }
        }
        
        entity.setWard(getWardOrNull(request.getWardId()));
        entity.setCustomerGroup(getCustomerGroupOrNull(request.getCustomerGroupId()));
        return gasBookMapper.toResponse(gasBookRepository.save(entity));
    }

    public GasBookResponse getById(UUID gasBookId) {
        return gasBookRepository.findById(gasBookId)
                .map(gasBookMapper::toResponse)
                .orElseThrow(() -> new ApiException(ErrorCode.GAS_BOOK_NOT_FOUND));
    }

    public PageResult<GasBookResponse> getAll(Integer page, Integer limit) {
        int pageNumber = page == null || page < 1 ? 0 : page - 1;
        int pageSize = limit == null || limit <= 0 ? 10 : limit;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("fullName").ascending());

        Page<GasBookEntity> gasBookPage = gasBookRepository.findAll(pageable);
        return buildPageResult(gasBookPage);
    }

    public PageResult<GasBookResponse> search(
            String keyword,
            String customerCode,
            String fullName,
            String phoneNumber,
            String customerGroup,
            Integer page,
            Integer limit
    ) {
        int pageNumber = page == null || page < 1 ? 0 : page - 1;
        int pageSize = limit == null || limit <= 0 ? 10 : limit;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("fullName").ascending());

        Page<GasBookEntity> gasBookPage = gasBookRepository.searchGasBooks(keyword, customerCode, fullName, phoneNumber, customerGroup, pageable);
        return buildPageResult(gasBookPage);
    }

    public GasBookResponse update(UUID gasBookId, GasBookUpdateRequest request) {
        GasBookEntity entity = gasBookRepository.findById(gasBookId)
                .orElseThrow(() -> new ApiException(ErrorCode.GAS_BOOK_NOT_FOUND));

        if (request.getGasBookCode() != null && !request.getGasBookCode().trim().isEmpty() 
            && !request.getGasBookCode().equalsIgnoreCase(entity.getGasBookCode())) {
            if (gasBookRepository.existsByGasBookCodeIgnoreCase(request.getGasBookCode())) {
                throw new ApiException(ErrorCode.CUSTOMER_CODE_ALREADY_EXISTS);
            }
        }

        gasBookMapper.updateEntity(request, entity);
        entity.setWard(getWardOrNull(request.getWardId()));
        entity.setCustomerGroup(getCustomerGroupOrNull(request.getCustomerGroupId()));

        return gasBookMapper.toResponse(gasBookRepository.save(entity));
    }

    public void delete(UUID gasBookId) {
        GasBookEntity entity = gasBookRepository.findById(gasBookId)
                .orElseThrow(() -> new ApiException(ErrorCode.GAS_BOOK_NOT_FOUND));
        gasBookRepository.delete(entity);
    }

    private WardEntity getWardOrNull(UUID wardId) {
        if (wardId == null) {
            return null;
        }
        return wardRepository.findById(wardId)
                .orElseThrow(() -> new ApiException(ErrorCode.WARD_NOT_FOUND));
    }

    private CustomerGroupEntity getCustomerGroupOrNull(UUID customerGroupId) {
        if (customerGroupId == null) {
            return null;
        }
        return customerGroupRepository.findById(customerGroupId)
                .orElseThrow(() -> new ApiException(ErrorCode.CUSTOMER_GROUP_NOT_FOUND));
    }

    private PageResult<GasBookResponse> buildPageResult(Page<GasBookEntity> pageResult) {
        return PageResult.<GasBookResponse>builder()
                .content(pageResult.getContent().stream().map(gasBookMapper::toResponse).toList())
                .page(pageResult.getNumber() + 1)
                .size(pageResult.getSize())
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .build();
    }

    public PageResult<GasBookHistoryResponse> getHistory(UUID gasBookId, Integer page, Integer limit) {
        int pageNumber = page == null || page < 1 ? 0 : page - 1;
        int pageSize = limit == null || limit <= 0 ? 10 : limit;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("invoiceDate").descending());

        Page<SaleInvoiceEntity> invoicePage = saleInvoiceRepository.findByGasBook_GasBookId(gasBookId, pageable);
        
        return PageResult.<GasBookHistoryResponse>builder()
                .content(invoicePage.getContent().stream().map(invoice -> GasBookHistoryResponse.builder()
                        .id(invoice.getInvoiceId())
                        .doc(invoice.getInvoiceCode())
                        .time(invoice.getInvoiceDate())
                        .value(invoice.getTotalAmount())
                        .debt(java.math.BigDecimal.ZERO) // Temporary placeholder for debt calculation
                        .build()).toList())
                .page(invoicePage.getNumber() + 1)
                .size(invoicePage.getSize())
                .totalElements(invoicePage.getTotalElements())
                .totalPages(invoicePage.getTotalPages())
                .build();
    }

    public PageResult<GasBookDetailedHistoryResponse> getDetailedHistory(UUID gasBookId, Integer page, Integer limit) {
        int pageNumber = page == null || page < 1 ? 0 : page - 1;
        int pageSize = limit == null || limit <= 0 ? 10 : limit;
        // Sorting by invoice date descending, but detailed repo doesn't directly have invoiceDate in the root, it's in invoice.invoiceDate
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("invoice.invoiceDate").descending());

        Page<SaleInvoiceDetailEntity> detailPage = saleInvoiceDetailRepository.findByInvoice_GasBook_GasBookId(gasBookId, pageable);
        
        return PageResult.<GasBookDetailedHistoryResponse>builder()
                .content(detailPage.getContent().stream().map(detail -> GasBookDetailedHistoryResponse.builder()
                        .id(detail.getId())
                        .doc(detail.getInvoice().getInvoiceCode())
                        .time(detail.getInvoice().getInvoiceDate())
                        .productName(detail.getProduct() != null ? detail.getProduct().getProductName() : "")
                        .quantity(detail.getQuantity())
                        .price(detail.getUnitPrice())
                        .total(detail.getTotal())
                        .note(detail.getInvoice().getNote())
                        .build()).toList())
                .page(detailPage.getNumber() + 1)
                .size(detailPage.getSize())
                .totalElements(detailPage.getTotalElements())
                .totalPages(detailPage.getTotalPages())
                .build();
    }

    private String generateGasBookCode() {
        String prefix = "SG";
        java.util.List<String> existingCodes = gasBookRepository.findGasBookCodesByPrefix(prefix);
        
        long maxNumber = 0;
        for (String code : existingCodes) {
            try {
                long num = Long.parseLong(code.substring(prefix.length()));
                if (num > maxNumber) {
                    maxNumber = num;
                }
            } catch (NumberFormatException e) {
                // Ignore parsing errors for non-numeric suffix
            }
        }
        
        return String.format("%s%05d", prefix, maxNumber + 1);
    }
}
