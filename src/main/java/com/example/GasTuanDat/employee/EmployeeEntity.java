package com.example.GasTuanDat.employee;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"Employee\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeEntity {
    @Id
    @Column(name = "\"employeeId\"")
    @GeneratedValue
    @UuidGenerator
    private UUID employeeId;

    @Column(name = "\"accountId\"")
    private UUID accountId;

    @Column(name = "\"employeeCode\"")
    private String employeeCode;

    @Column(name = "\"departmentId\"")
    private UUID departmentId;

    @Column(name = "\"note\"")
    private String note;

    @Column(name = "\"status\"")
    private Boolean status;

    @Column(name = "\"hireDate\"")
    private  LocalDate hireDate;
}
