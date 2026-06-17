package com.example.GasTuanDat.employee;

import com.example.GasTuanDat.common.exception.ApiException;
import com.example.GasTuanDat.common.exception.ErrorCode;
import com.example.GasTuanDat.common.response.PageResult;
import com.example.GasTuanDat.employee.dtos.EmployeeCreateRequest;
import com.example.GasTuanDat.employee.dtos.EmployeeResponse;
import com.example.GasTuanDat.employee.dtos.EmployeeUpdateRequest;
import com.example.GasTuanDat.employee.entities.EmployeeEntity;
import com.example.GasTuanDat.employee.mapper.EmployeeMapper;
import com.example.GasTuanDat.position.PositionRepository;
import com.example.GasTuanDat.position.entities.PositionEntity;
import java.time.LocalDate;
import java.util.List;
import java.text.Normalizer;
import java.util.UUID;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.GasTuanDat.ward.WardRepository;
import com.example.GasTuanDat.ward.entities.WardEntity;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final PositionRepository positionRepository;
    private final WardRepository wardRepository;

    public EmployeeEntity create(EmployeeCreateRequest request) {
        PositionEntity position = positionRepository.findById(request.getPositionId())
                .orElseThrow(() -> new ApiException(ErrorCode.POSITION_NOT_FOUND));

        String employeeCode = generateEmployeeCode(position.getName());
        while (employeeRepository.findByEmployeeCode(employeeCode).isPresent()) {
            employeeCode = generateEmployeeCode(position.getName(), getNextSequence(employeeCode));
        }

        EmployeeEntity employee = employeeMapper.toEntity(request);
        employee.setEmployeeCode(employeeCode);
        employee.setPosition(position);
        
        if (request.getWardId() != null) {
            WardEntity ward = wardRepository.findById(request.getWardId())
                    .orElseThrow(() -> new ApiException(ErrorCode.WARD_NOT_FOUND));
            employee.setWard(ward);
        }

        if (employee.getStatus() == null) {
            employee.setStatus(true);
        }
        
        if (employee.getHireDate() == null) {
            employee.setHireDate(LocalDate.now());
        }

        return employeeRepository.save(employee);
    }

    private String generateEmployeeCode(String positionName) {
        return generateEmployeeCode(positionName, 1);
    }

    private String generateEmployeeCode(String positionName, int sequence) {
        return normalizePositionName(positionName) + sequence;
    }

    private int getNextSequence(String employeeCode) {
        int index = employeeCode.length() - 1;
        while (index >= 0 && Character.isDigit(employeeCode.charAt(index))) {
            index--;
        }

        if (index == employeeCode.length() - 1) {
            return 1;
        }

        String numericPart = employeeCode.substring(index + 1);
        return Integer.parseInt(numericPart) + 1;
    }

    private String normalizePositionName(String positionName) {
        String normalized = Normalizer.normalize(positionName, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^A-Za-z0-9]", "")
                .toUpperCase(Locale.ROOT);

        if (normalized.isBlank()) {
            throw new ApiException(ErrorCode.INVALID_INPUT);
        }

        return normalized;
    }

    public List<EmployeeEntity> getAll() {
        return employeeRepository.findAll();
    }

    public PageResult<EmployeeResponse> search(
            String keyword,
            String jobTitle,
            Integer page,
            Integer limit
    ) {
        int pageNumber = page == null || page < 1 ? 0 : page - 1;
        int pageSize = limit == null || limit <= 0 ? 10 : limit;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("fullName").ascending());

        Page<EmployeeEntity> employeePage = employeeRepository.searchEmployees(keyword, jobTitle, pageable);

        return PageResult.<EmployeeResponse>builder()
                .content(employeePage.getContent().stream().map(employeeMapper::toResponse).toList())
                .page(employeePage.getNumber() + 1)
                .size(employeePage.getSize())
                .totalElements(employeePage.getTotalElements())
                .totalPages(employeePage.getTotalPages())
                .build();
    }

    public EmployeeEntity getById(UUID employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ApiException(ErrorCode.EMPLOYEE_NOT_FOUND));
    }

    public EmployeeEntity getByEmployeeCode(String employeeCode) {
        return employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new ApiException(ErrorCode.EMPLOYEE_NOT_FOUND));
    }

    public EmployeeEntity update(UUID employeeId, EmployeeUpdateRequest request) {
        EmployeeEntity employee = getById(employeeId);
        employeeMapper.updateEntity(request, employee);
        
        if (request.getPositionId() != null) {
            PositionEntity position = positionRepository.findById(request.getPositionId())
                    .orElseThrow(() -> new ApiException(ErrorCode.POSITION_NOT_FOUND));
            employee.setPosition(position);
        }
        
        if (request.getWardId() != null) {
            WardEntity ward = wardRepository.findById(request.getWardId())
                    .orElseThrow(() -> new ApiException(ErrorCode.WARD_NOT_FOUND));
            employee.setWard(ward);
        }
        
        return employeeRepository.save(employee);
    }

    public void delete(UUID employeeId) {
        EmployeeEntity employee = getById(employeeId);
        employeeRepository.delete(employee);
    }
}
