package com.example.GasTuanDat.debtReceipt;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.GasTuanDat.common.exception.ApiException;
import com.example.GasTuanDat.common.exception.ErrorCode;
import com.example.GasTuanDat.common.response.PageResult;
import com.example.GasTuanDat.customer.CustomerRepository;
import com.example.GasTuanDat.customer.entities.CustomerEntity;
import com.example.GasTuanDat.debtReceipt.dtos.DebtReceiptCreateRequest;
import com.example.GasTuanDat.debtReceipt.dtos.DebtReceiptDetailCreateRequest;
import com.example.GasTuanDat.debtReceipt.dtos.DebtReceiptDetailResponse;
import com.example.GasTuanDat.debtReceipt.dtos.DebtReceiptResponse;
import com.example.GasTuanDat.debtReceipt.dtos.DebtReceiptUpdateRequest;
import com.example.GasTuanDat.debtReceipt.entities.DebtReceiptDetailEntity;
import com.example.GasTuanDat.debtReceipt.entities.DebtReceiptEntity;
import com.example.GasTuanDat.debtReceipt.mapper.DebtReceiptMapper;
import com.example.GasTuanDat.gasBook.GasBookRepository;
import com.example.GasTuanDat.gasBook.entities.GasBookEntity;
import com.example.GasTuanDat.product.ProductRepository;
import com.example.GasTuanDat.product.entities.ProductEntity;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DebtReceiptService {

    private final DebtReceiptRepository debtReceiptRepository;
    private final DebtReceiptDetailRepository debtReceiptDetailRepository;
    private final CustomerRepository customerRepository;
    private final GasBookRepository gasBookRepository;
    private final ProductRepository productRepository;
    private final DebtReceiptMapper debtReceiptMapper;

    @Transactional
    public DebtReceiptResponse create(DebtReceiptCreateRequest request) {
        DebtReceiptEntity entity = debtReceiptMapper.toEntity(request);

        if (entity.getReceiptCode() == null || entity.getReceiptCode().trim().isEmpty() || entity.getReceiptCode().equals("Mã tự động")) {
            entity.setReceiptCode(generateReceiptCode());
        } else {
            if (debtReceiptRepository.existsByReceiptCodeIgnoreCase(entity.getReceiptCode())) {
                throw new ApiException(ErrorCode.PROMOTION_CODE_ALREADY_EXISTS); // Using generic for now
            }
        }

        resolveCustomer(entity, request.getCustomerCode());

        DebtReceiptEntity saved = debtReceiptRepository.save(entity);

        BigDecimal totalDebt = BigDecimal.ZERO;

        if (request.getItems() != null && !request.getItems().isEmpty()) {
            totalDebt = saveDetails(saved, request.getItems());
        }

        // Cập nhật công nợ nếu trạng thái là Chưa trả nợ
        if ("Chưa trả nợ".equals(saved.getStatus())) {
            updateCustomerDebt(saved, totalDebt, true);
        }

        return mapToResponse(saved);
    }

    public DebtReceiptResponse getById(UUID receiptId) {
        DebtReceiptEntity entity = debtReceiptRepository.findById(receiptId)
                .orElseThrow(() -> new ApiException(ErrorCode.PROMOTION_NOT_FOUND));
        return mapToResponse(entity);
    }

    @Transactional
    public DebtReceiptResponse update(UUID receiptId, DebtReceiptUpdateRequest request) {
        DebtReceiptEntity entity = debtReceiptRepository.findById(receiptId)
                .orElseThrow(() -> new ApiException(ErrorCode.PROMOTION_NOT_FOUND));

        String oldStatus = entity.getStatus();
        BigDecimal oldTotal = calculateTotal(entity);

        if (request.getCode() != null && !request.getCode().trim().isEmpty() && !request.getCode().equals("Mã tự động")
            && !request.getCode().equalsIgnoreCase(entity.getReceiptCode())) {
            if (debtReceiptRepository.existsByReceiptCodeIgnoreCase(request.getCode())) {
                throw new ApiException(ErrorCode.PROMOTION_CODE_ALREADY_EXISTS);
            }
            entity.setReceiptCode(request.getCode());
        }

        debtReceiptMapper.updateEntity(request, entity);
        resolveCustomer(entity, request.getCustomerCode());

        DebtReceiptEntity saved = debtReceiptRepository.save(entity);

        BigDecimal newTotal = BigDecimal.ZERO;
        if (request.getItems() != null) {
            debtReceiptDetailRepository.deleteAll(entity.getDetails());
            entity.getDetails().clear();
            newTotal = saveDetails(saved, request.getItems());
        } else {
            newTotal = oldTotal;
        }

        // Logic cập nhật công nợ
        if ("Chưa trả nợ".equals(oldStatus) && "Đã trả nợ".equals(saved.getStatus())) {
            // Trừ nợ
            updateCustomerDebt(saved, oldTotal, false);
        } else if ("Đã trả nợ".equals(oldStatus) && "Chưa trả nợ".equals(saved.getStatus())) {
            // Cộng nợ lại
            updateCustomerDebt(saved, newTotal, true);
        } else if ("Chưa trả nợ".equals(oldStatus) && "Chưa trả nợ".equals(saved.getStatus())) {
            // Cập nhật phần chênh lệch
            BigDecimal diff = newTotal.subtract(oldTotal);
            if (diff.compareTo(BigDecimal.ZERO) > 0) {
                updateCustomerDebt(saved, diff, true);
            } else if (diff.compareTo(BigDecimal.ZERO) < 0) {
                updateCustomerDebt(saved, diff.abs(), false);
            }
        }

        return mapToResponse(saved);
    }

    @Transactional
    public void delete(UUID receiptId) {
        DebtReceiptEntity entity = debtReceiptRepository.findById(receiptId)
                .orElseThrow(() -> new ApiException(ErrorCode.PROMOTION_NOT_FOUND));

        if ("Chưa trả nợ".equals(entity.getStatus())) {
            BigDecimal total = calculateTotal(entity);
            updateCustomerDebt(entity, total, false); // Trừ nợ đi vì phiếu nợ bị xóa
        }

        debtReceiptRepository.delete(entity);
    }

    public PageResult<DebtReceiptResponse> search(String keyword, LocalDate startDate, LocalDate endDate, LocalDate dueStartDate, LocalDate dueEndDate, Integer page, Integer limit) {
        int pageNumber = page == null || page < 1 ? 0 : page - 1;
        int pageSize = limit == null || limit <= 0 ? 10 : limit;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("debtDate").descending());

        Page<DebtReceiptEntity> entityPage = debtReceiptRepository.searchDebtReceipts(keyword, startDate, endDate, dueStartDate, dueEndDate, pageable);
        
        return PageResult.<DebtReceiptResponse>builder()
                .content(entityPage.getContent().stream().map(this::mapToResponse).toList())
                .page(entityPage.getNumber() + 1)
                .size(entityPage.getSize())
                .totalElements(entityPage.getTotalElements())
                .totalPages(entityPage.getTotalPages())
                .build();
    }

    private void resolveCustomer(DebtReceiptEntity entity, String customerCode) {
        if (customerCode == null) return;
        if (customerCode.startsWith("SG")) {
            GasBookEntity gasBook = gasBookRepository.findByGasBookCodeIgnoreCase(customerCode)
                .orElseGet(() -> gasBookRepository.findAll().stream()
                .filter(g -> g.getGasBookCode() != null && g.getGasBookCode().equalsIgnoreCase(customerCode)).findFirst()
                .orElseThrow(() -> new ApiException(ErrorCode.CUSTOMER_NOT_FOUND)));
            entity.setGasBook(gasBook);
            entity.setCustomer(null);
        } else {
            CustomerEntity customer = customerRepository.findByCustomerCodeIgnoreCase(customerCode)
                    .orElseThrow(() -> new ApiException(ErrorCode.CUSTOMER_NOT_FOUND));
            entity.setCustomer(customer);
            entity.setGasBook(null);
        }
    }

    private BigDecimal saveDetails(DebtReceiptEntity receipt, List<DebtReceiptDetailCreateRequest> requests) {
        BigDecimal total = BigDecimal.ZERO;
        List<DebtReceiptDetailEntity> details = requests.stream().map(req -> {
            ProductEntity product = productRepository.findById(req.getProductId())
                    .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
            return DebtReceiptDetailEntity.builder()
                    .debtReceipt(receipt)
                    .product(product)
                    .quantity(req.getQuantity())
                    .price(req.getPrice())
                    .priceList(req.getPriceList())
                    .build();
        }).collect(Collectors.toList());

        for (DebtReceiptDetailEntity d : details) {
            if (d.getPrice() != null && d.getQuantity() != null) {
                total = total.add(d.getPrice().multiply(BigDecimal.valueOf(d.getQuantity())));
            }
        }

        debtReceiptDetailRepository.saveAll(details);
        receipt.getDetails().addAll(details);
        return total;
    }

    private BigDecimal calculateTotal(DebtReceiptEntity entity) {
        BigDecimal total = BigDecimal.ZERO;
        if (entity.getDetails() != null) {
            for (DebtReceiptDetailEntity d : entity.getDetails()) {
                if (d.getPrice() != null && d.getQuantity() != null) {
                    total = total.add(d.getPrice().multiply(BigDecimal.valueOf(d.getQuantity())));
                }
            }
        }
        return total;
    }

    private void updateCustomerDebt(DebtReceiptEntity entity, BigDecimal amount, boolean isAdd) {
        if (entity.getCustomer() != null) {
            CustomerEntity cus = entity.getCustomer();
            BigDecimal current = cus.getDebt() == null ? BigDecimal.ZERO : cus.getDebt();
            cus.setDebt(isAdd ? current.add(amount) : current.subtract(amount));
            customerRepository.save(cus);
        } else if (entity.getGasBook() != null) {
            GasBookEntity gas = entity.getGasBook();
            BigDecimal current = gas.getDebt() == null ? BigDecimal.ZERO : gas.getDebt();
            gas.setDebt(isAdd ? current.add(amount) : current.subtract(amount));
            gasBookRepository.save(gas);
        }
    }

    private DebtReceiptResponse mapToResponse(DebtReceiptEntity entity) {
        DebtReceiptResponse response = debtReceiptMapper.toResponse(entity);
        response.setId(entity.getReceiptId());
        response.setCode(entity.getReceiptCode());
        
        if (entity.getCustomer() != null) {
            response.setCustomerCode(entity.getCustomer().getCustomerCode());
            response.setCustomerName(entity.getCustomer().getFullName());
        } else if (entity.getGasBook() != null) {
            response.setCustomerCode(entity.getGasBook().getGasBookCode());
            response.setCustomerName(entity.getGasBook().getFullName());
        }

        response.setItems(entity.getDetails().stream().map(d -> DebtReceiptDetailResponse.builder()
                .id(d.getId())
                .productId(d.getProduct().getProductId())
                .productCode(d.getProduct().getProductCode())
                .productName(d.getProduct().getProductName())
                .quantity(d.getQuantity())
                .price(d.getPrice())
                .priceList(d.getPriceList())
                .build()).toList());
        return response;
    }

    private String generateReceiptCode() {
        String prefix = "PN";
        List<String> existingCodes = debtReceiptRepository.findReceiptCodesByPrefix(prefix);
        
        long maxNumber = 0;
        for (String code : existingCodes) {
            try {
                long num = Long.parseLong(code.substring(prefix.length()));
                if (num > maxNumber) {
                    maxNumber = num;
                }
            } catch (NumberFormatException e) {
                // Ignore parsing errors
            }
        }
        
        return String.format("%s%05d", prefix, maxNumber + 1);
    }
}
