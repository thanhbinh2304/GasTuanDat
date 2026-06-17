package com.example.GasTuanDat.employee.entities;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import com.example.GasTuanDat.position.entities.PositionEntity;
import com.example.GasTuanDat.ward.entities.WardEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Column(name = "\"employeeCode\"")
    private String employeeCode;

    @Column(name  = "\"fullName\"")
    private String fullName;

    @JoinColumn(name = "\"positionId\"")
    @ManyToOne(fetch = FetchType.EAGER)
    private PositionEntity position;

    @Column(name = "\"gender\"")
    private String gender;

    @Column(name = "\"dateOfBirth\"")
    private LocalDate dateOfBirth;

    @Column(name = "\"phoneNumber\"")
    private String phoneNumber;

    @Column(name= "\"email\"")
    private String email;

    @Column(name = "\"note\"")
    private String note;

    @Column(name = "\"status\"")
    private Boolean status;

    @Column(name = "\"hireDate\"")
    private LocalDate hireDate;

    @JoinColumn(name = "\"wardId\"")
    @ManyToOne
    private WardEntity ward;

    @CreationTimestamp
    @Column(name = "\"createdAt\"", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "\"updatedAt\"")
    private Timestamp updatedAt;
}
