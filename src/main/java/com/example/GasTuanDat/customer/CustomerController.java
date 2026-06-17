package com.example.GasTuanDat.customer;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.GasTuanDat.common.response.ApiResponse;
import com.example.GasTuanDat.common.constants.RoleConstants;
import com.example.GasTuanDat.customer.dtos.CustomerCreateRequest;
import com.example.GasTuanDat.customer.dtos.CustomerResponse;
import com.example.GasTuanDat.common.response.PageResult;
import com.example.GasTuanDat.customer.dtos.CustomerUpdateRequest;
import com.example.GasTuanDat.customer.dtos.CustomerGroupResponse;
import com.example.GasTuanDat.gasBook.dtos.GasBookHistoryResponse;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Customer", description = "Customer management APIs")
public class CustomerController {
    CustomerService customerService;

    @PostMapping
    @PreAuthorize("hasAnyRole('" + RoleConstants.ADMIN + "', '" + RoleConstants.SALES + "')")
    @Operation(summary = "Create customer")
    public ApiResponse<CustomerResponse> create(@Valid @RequestBody CustomerCreateRequest request) {
        return ApiResponse.<CustomerResponse>builder()
                .code(200)
                .message("Create customer success")
                .data(customerService.create(request))
                .build();
    }

    @GetMapping("/{customerId}")
    @PreAuthorize("hasAnyRole('" + RoleConstants.ADMIN + "', '" + RoleConstants.SALES + "')")
    @Operation(summary = "Get customer by id")
    public ApiResponse<CustomerResponse> getById(@PathVariable UUID customerId) {
        return ApiResponse.<CustomerResponse>builder()
                .code(200)
                .message("Get customer success")
                .data(customerService.getById(customerId))
                .build();
    }

    @PutMapping("/{customerId}")
    @PreAuthorize("hasAnyRole('" + RoleConstants.ADMIN + "', '" + RoleConstants.SALES + "')")
    @Operation(summary = "Update customer")
    public ApiResponse<CustomerResponse> update(@PathVariable UUID customerId,
            @Valid @RequestBody CustomerUpdateRequest request) {
        return ApiResponse.<CustomerResponse>builder()
                .code(200)
                .message("Update customer success")
                .data(customerService.update(customerId, request))
                .build();
    }

    @DeleteMapping("/{customerId}")
    @PreAuthorize("hasAnyRole('" + RoleConstants.ADMIN + "', '" + RoleConstants.SALES + "')")
    @Operation(summary = "Delete customer")
    public ApiResponse<Void> delete(@PathVariable UUID customerId) {
        customerService.delete(customerId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete customer success")
                .build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('" + RoleConstants.ADMIN + "', '" + RoleConstants.SALES + "', '" + RoleConstants.WAREHOUSE
            + "')")
    @Operation(summary = "Search customers with filters and pagination")
    public ApiResponse<PageResult<CustomerResponse>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean gender,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lastTransactionDate,
            @RequestParam(required = false) String customerGroup,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "fullName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ApiResponse.<PageResult<CustomerResponse>>builder()
                .code(200)
                .message("Search customers success")
                .data(customerService.search(
                        keyword,
                        gender,
                        dateOfBirth,
                        address,
                        lastTransactionDate,
                        customerGroup,
                        page,
                        size,
                        sortBy,
                        sortDir))
                .build();
    }

    @GetMapping("/groups")
    @PreAuthorize("hasAnyRole('" + RoleConstants.ADMIN + "', '" + RoleConstants.SALES + "', '" + RoleConstants.WAREHOUSE + "')")
    @Operation(summary = "Get all customer groups")
    public ApiResponse<List<CustomerGroupResponse>> getAllGroups() {
        return ApiResponse.<List<CustomerGroupResponse>>builder()
                .code(200)
                .message("Get all customer groups success")
                .data(customerService.getAllCustomerGroups())
                .build();
    }

    @PostMapping("/groups")
    @PreAuthorize("hasAnyRole('" + RoleConstants.ADMIN + "', '" + RoleConstants.SALES + "')")
    @Operation(summary = "Create a customer group")
    public ApiResponse<CustomerGroupResponse> createGroup(@RequestBody java.util.Map<String, String> request) {
        String groupName = request.get("groupName");
        return ApiResponse.<CustomerGroupResponse>builder()
                .code(200)
                .message("Create customer group success")
                .data(customerService.createCustomerGroup(groupName))
                .build();
    }

    @PutMapping("/groups/{groupId}")
    @PreAuthorize("hasAnyRole('" + RoleConstants.ADMIN + "', '" + RoleConstants.SALES + "')")
    @Operation(summary = "Update a customer group")
    public ApiResponse<CustomerGroupResponse> updateGroup(@PathVariable UUID groupId, @RequestBody java.util.Map<String, String> request) {
        String groupName = request.get("groupName");
        return ApiResponse.<CustomerGroupResponse>builder()
                .code(200)
                .message("Update customer group success")
                .data(customerService.updateCustomerGroup(groupId, groupName))
                .build();
    }

    @DeleteMapping("/groups/{groupId}")
    @PreAuthorize("hasAnyRole('" + RoleConstants.ADMIN + "', '" + RoleConstants.SALES + "')")
    @Operation(summary = "Delete a customer group")
    public ApiResponse<Void> deleteGroup(@PathVariable UUID groupId) {
        customerService.deleteCustomerGroup(groupId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete customer group success")
                .build();
    }

    @GetMapping("/{id}/history")
    @PreAuthorize("hasAnyRole('" + RoleConstants.ADMIN + "', '" + RoleConstants.SALES + "')")
    @Operation(summary = "Get sale history of a customer")
    public ApiResponse<PageResult<GasBookHistoryResponse>> getHistory(
            @PathVariable UUID id,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        return ApiResponse.<PageResult<GasBookHistoryResponse>>builder()
                .code(200)
                .message("Get customer history success")
                .data(customerService.getHistory(id, page, limit))
                .build();
    }
}
