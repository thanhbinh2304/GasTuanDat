package com.example.GasTuanDat.customer;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.GasTuanDat.common.exception.ApiException;
import com.example.GasTuanDat.common.exception.ErrorCode;
import com.example.GasTuanDat.customer.dtos.CustomerCreateRequest;
import com.example.GasTuanDat.common.response.PageResult;
import com.example.GasTuanDat.customer.dtos.CustomerResponse;
import com.example.GasTuanDat.customer.dtos.CustomerUpdateRequest;
import com.example.GasTuanDat.customer.entities.CustomerEntity;
import com.example.GasTuanDat.customer.entities.CustomerGroupEntity;
import com.example.GasTuanDat.customer.mapper.CustomerMapper;
import com.example.GasTuanDat.ward.WardRepository;
import com.example.GasTuanDat.ward.entities.WardEntity;
import com.example.GasTuanDat.customer.dtos.CustomerGroupResponse;
import com.example.GasTuanDat.gasBook.dtos.GasBookHistoryResponse;
import com.example.GasTuanDat.sale.SaleInvoiceRepository;
import com.example.GasTuanDat.sale.entities.SaleInvoiceEntity;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerGroupRepository customerGroupRepository;
    private final WardRepository wardRepository;
    private final CustomerMapper customerMapper;
    private final SaleInvoiceRepository saleInvoiceRepository;

    public CustomerResponse create(CustomerCreateRequest request) {
        CustomerEntity customer = customerMapper.toEntity(request);

        if (customer.getCustomerCode() == null || customer.getCustomerCode().trim().isEmpty()) {
            customer.setCustomerCode(generateCustomerCode());
        } else {
            if (customerRepository.existsByCustomerCodeIgnoreCase(customer.getCustomerCode())) {
                throw new ApiException(ErrorCode.CUSTOMER_CODE_ALREADY_EXISTS);
            }
        }

        if (customer.getEmail() != null && customer.getEmail().trim().isEmpty()) {
            customer.setEmail(null);
        }
        customer.setWard(getWardOrNull(request.getWardId()));
        customer.setCustomerGroup(getCustomerGroupOrNull(request.getCustomerGroupId()));
        CustomerEntity saved = customerRepository.save(customer);
        return customerMapper.toResponse(saved,
                customerRepository.findLastTransactionDateByCustomerId(saved.getCustomerId()));
    }

    public CustomerResponse getById(UUID customerId) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ApiException(ErrorCode.CUSTOMER_NOT_FOUND));
        return customerMapper.toResponse(customer, customerRepository.findLastTransactionDateByCustomerId(customerId));
    }

    public CustomerResponse update(UUID customerId, CustomerUpdateRequest request) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ApiException(ErrorCode.CUSTOMER_NOT_FOUND));

        if (request.getCustomerCode() != null && !request.getCustomerCode().trim().isEmpty() 
            && !request.getCustomerCode().equalsIgnoreCase(customer.getCustomerCode())) {
            if (customerRepository.existsByCustomerCodeIgnoreCase(request.getCustomerCode())) {
                throw new ApiException(ErrorCode.CUSTOMER_CODE_ALREADY_EXISTS);
            }
        }

        customerMapper.updateEntity(request, customer);
        
        if (customer.getEmail() != null && customer.getEmail().trim().isEmpty()) {
            customer.setEmail(null);
        }

        if (request.getWardId() != null) {
            customer.setWard(getWardOrNull(request.getWardId()));
        }

        if (request.getCustomerGroupId() != null) {
            customer.setCustomerGroup(getCustomerGroupOrNull(request.getCustomerGroupId()));
        }

        CustomerEntity saved = customerRepository.save(customer);
        return customerMapper.toResponse(saved,
                customerRepository.findLastTransactionDateByCustomerId(saved.getCustomerId()));
    }

    public void delete(UUID customerId) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ApiException(ErrorCode.CUSTOMER_NOT_FOUND));
        customerRepository.delete(customer);
    }

    public PageResult<com.example.GasTuanDat.customer.dtos.CustomerResponse> search(
            String keyword,
            Boolean gender,
            LocalDate dateOfBirth,
            String address,
            LocalDate lastTransactionDate,
            String customerGroup,
            Integer page,
            Integer size,
            String sortBy,
            String sortDir) {
        int pageNumber = page == null || page < 0 ? 0 : page;
        int pageSize = size == null || size <= 0 ? 10 : size;

        Sort sort = "desc".equalsIgnoreCase(sortDir)
                ? Sort.by(sortBy == null || sortBy.isBlank() ? "fullName" : sortBy).descending()
                : Sort.by(sortBy == null || sortBy.isBlank() ? "fullName" : sortBy).ascending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        OffsetDateTime lastTransactionFrom = null;
        OffsetDateTime lastTransactionTo = null;
        if (lastTransactionDate != null) {
            lastTransactionFrom = lastTransactionDate.atStartOfDay().atOffset(ZoneOffset.UTC);
            lastTransactionTo = lastTransactionDate.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC);
        }

        Page<CustomerEntity> customerPage = customerRepository.searchCustomers(
                keyword,
                gender,
                dateOfBirth,
                address,
                customerGroup,
                lastTransactionFrom != null,
                lastTransactionFrom,
                lastTransactionTo != null,
                lastTransactionTo,
                pageable);
        return PageResult.<com.example.GasTuanDat.customer.dtos.CustomerResponse>builder()
                .content(customerPage.getContent().stream()
                        .map(customer -> customerMapper.toResponse(
                                customer,
                                customerRepository.findLastTransactionDateByCustomerId(customer.getCustomerId())))
                        .toList())
                .page(customerPage.getNumber())
                .size(customerPage.getSize())
                .totalElements(customerPage.getTotalElements())
                .totalPages(customerPage.getTotalPages())
                .build();
    }

    public List<CustomerGroupResponse> getAllCustomerGroups() {
        return customerGroupRepository.findAll().stream()
                .map(group -> CustomerGroupResponse.builder()
                        .customerGroupId(group.getCustomerGroupId())
                        .groupName(group.getGroupName())
                        .build())
                .collect(Collectors.toList());
    }

    public CustomerGroupResponse createCustomerGroup(String groupName) {
        CustomerGroupEntity group = CustomerGroupEntity.builder()
                .groupName(groupName)
                .build();
        CustomerGroupEntity saved = customerGroupRepository.save(group);
        return CustomerGroupResponse.builder()
                .customerGroupId(saved.getCustomerGroupId())
                .groupName(saved.getGroupName())
                .build();
    }

    public CustomerGroupResponse updateCustomerGroup(UUID groupId, String groupName) {
        CustomerGroupEntity group = customerGroupRepository.findById(groupId)
                .orElseThrow(() -> new ApiException(ErrorCode.CUSTOMER_GROUP_NOT_FOUND));
        group.setGroupName(groupName);
        CustomerGroupEntity saved = customerGroupRepository.save(group);
        return CustomerGroupResponse.builder()
                .customerGroupId(saved.getCustomerGroupId())
                .groupName(saved.getGroupName())
                .build();
    }

    public void deleteCustomerGroup(UUID groupId) {
        CustomerGroupEntity group = customerGroupRepository.findById(groupId)
                .orElseThrow(() -> new ApiException(ErrorCode.CUSTOMER_GROUP_NOT_FOUND));
        customerGroupRepository.delete(group);
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

    public PageResult<GasBookHistoryResponse> getHistory(UUID customerId, Integer page, Integer limit) {
        int pageNumber = page == null || page < 1 ? 0 : page - 1;
        int pageSize = limit == null || limit <= 0 ? 10 : limit;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("invoiceDate").descending());

        Page<SaleInvoiceEntity> invoicePage = saleInvoiceRepository.findByCustomer_CustomerIdAndOrderType(customerId, "Xuathang", pageable);

        return PageResult.<GasBookHistoryResponse>builder()
                .content(invoicePage.getContent().stream().map(invoice -> GasBookHistoryResponse.builder()
                        .id(invoice.getInvoiceId())
                        .doc(invoice.getInvoiceCode())
                        .time(invoice.getInvoiceDate())
                        .value(invoice.getTotalAmount())
                        .debt(invoice.getPaidAmount() != null && invoice.getTotalAmount() != null
                                ? invoice.getTotalAmount().subtract(invoice.getPaidAmount())
                                : java.math.BigDecimal.ZERO)
                        .build()).toList())
                .page(invoicePage.getNumber() + 1)
                .size(invoicePage.getSize())
                .totalElements(invoicePage.getTotalElements())
                .totalPages(invoicePage.getTotalPages())
                .build();
    }

    private String generateCustomerCode() {
        String prefix = "KH";
        List<String> existingCodes = customerRepository.findCustomerCodesByPrefix(prefix);
        
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
