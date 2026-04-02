package com.example.GasTuanDat.employee;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.GasTuanDat.common.exception.ApiException;
import com.example.GasTuanDat.common.exception.ErrorCode;
import com.example.GasTuanDat.employee.dtos.EmployeeCreateRequest;
import com.example.GasTuanDat.employee.dtos.EmployeeUpdateRequest;
import com.example.GasTuanDat.employee.mapper.EmployeeMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeEntity create(EmployeeCreateRequest request) {
        employeeRepository.findByEmployeeCode(request.getEmployeeCode())
                .ifPresent(employee -> {
                    throw new ApiException(ErrorCode.INVALID_INPUT);
                });

        EmployeeEntity employee = employeeMapper.toEntity(request);
        if (employee.getStatus() == null) {
            employee.setStatus(true);
        }
        return employeeRepository.save(employee);
    }

    public List<EmployeeEntity> getAll() {
        return employeeRepository.findAll();
    }

    public EmployeeEntity getById(UUID employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ApiException(ErrorCode.EMPLOYEE_NOT_FOUND));
    }

    public EmployeeEntity update(UUID employeeId, EmployeeUpdateRequest request) {
        EmployeeEntity employee = getById(employeeId);
        employeeMapper.updateEntity(request, employee);
        return employeeRepository.save(employee);
    }

    public void delete(UUID employeeId) {
        EmployeeEntity employee = getById(employeeId);
        employeeRepository.delete(employee);
    }
}
