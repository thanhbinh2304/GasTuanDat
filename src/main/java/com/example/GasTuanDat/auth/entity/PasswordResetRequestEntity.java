package com.example.GasTuanDat.auth.entity;

import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"PasswordResetRequest\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetRequestEntity {
    @Id
    @Column(name = "\"requestId\"")
    @GeneratedValue
    @UuidGenerator
    private UUID requestId;

    @Column(name = "\"accountId\"")
    private UUID accountId;

    @Column(name = "\"username\"")
    private String username;

    @Column(name = "\"employeeEmail\"")
    private String employeeEmail;

    @Column(name = "\"approvalToken\"")
    private UUID approvalToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "\"status\"")
    private PasswordResetStatus status;

    @Column(name = "\"processedAt\"")
    private Timestamp processedAt;

    @CreationTimestamp
    @Column(name = "\"createdAt\"", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "\"updatedAt\"")
    private Timestamp updatedAt;
}
