package com.example.GasTuanDat.account.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.example.GasTuanDat.employee.entities.EmployeeEntity;
import com.example.GasTuanDat.role.entity.RoleEntity;

@Entity
@Table(name = "\"Account\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountEntity {
    @Id
    @Column(name = "\"accountId\"")
    @GeneratedValue
    @UuidGenerator
    private UUID accountId;

    @Column(name = "\"username\"")
    private String username;

    @Column(name = "\"password\"")
    private String password;

    @Builder.Default
    @Column(name = "\"status\"")
    private Boolean status = true;

    @ManyToOne
    @JoinColumn(name = "\"roleId\"")
    private RoleEntity role;

    @ManyToOne
    @JoinColumn(name = "\"employeeId\"")
    private EmployeeEntity employee;

    @CreationTimestamp
    @Column(name = "\"createdAt\"", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "\"updatedAt\"")
    private Timestamp updatedAt;

    @Column(name = "\"deleteAt\"")
    private LocalDateTime deleteAt;
}
