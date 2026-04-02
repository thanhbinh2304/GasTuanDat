package com.example.GasTuanDat.employee;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.GasTuanDat.common.response.ApiResponse;
import com.example.GasTuanDat.employee.dtos.EmployeeCreateRequest;
import com.example.GasTuanDat.employee.dtos.EmployeeResponse;
import com.example.GasTuanDat.employee.dtos.EmployeeUpdateRequest;
import com.example.GasTuanDat.employee.mapper.EmployeeMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Employee", description = "Employee management APIs")
public class EmployeeController {
    EmployeeService employeeService;
    EmployeeMapper employeeMapper;

    @PostMapping
    @Operation(summary = "Create employee")
    public ApiResponse<EmployeeResponse> create(@Valid @RequestBody EmployeeCreateRequest request) {
        return ApiResponse.<EmployeeResponse>builder()
                .code(200)
                .message("Create employee success")
                .data(employeeMapper.toResponse(employeeService.create(request)))
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all employees")
    public ApiResponse<List<EmployeeResponse>> getAll() {
        return ApiResponse.<List<EmployeeResponse>>builder()
                .code(200)
                .message("Get employees success")
                .data(employeeService.getAll().stream().map(employeeMapper::toResponse).toList())
                .build();
    }

    @GetMapping("/{employeeId}")
    @Operation(summary = "Get employee by id")
    public ApiResponse<EmployeeResponse> getById(@PathVariable UUID employeeId) {
        return ApiResponse.<EmployeeResponse>builder()
                .code(200)
                .message("Get employee success")
                .data(employeeMapper.toResponse(employeeService.getById(employeeId)))
                .build();
    }

    @PutMapping("/{employeeId}")
    @Operation(summary = "Update employee")
    public ApiResponse<EmployeeResponse> update(@PathVariable UUID employeeId,
            @RequestBody EmployeeUpdateRequest request) {
        return ApiResponse.<EmployeeResponse>builder()
                .code(200)
                .message("Update employee success")
                .data(employeeMapper.toResponse(employeeService.update(employeeId, request)))
                .build();
    }

    @DeleteMapping("/{employeeId}")
    @Operation(summary = "Delete employee")
    public ApiResponse<Void> delete(@PathVariable UUID employeeId) {
        employeeService.delete(employeeId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete employee success")
                .build();
    }
}
