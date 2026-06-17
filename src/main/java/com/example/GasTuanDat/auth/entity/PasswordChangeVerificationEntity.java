package com.example.GasTuanDat.auth.entity;

import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

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
@Table(name = "\"PasswordChangeVerification\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordChangeVerificationEntity {
    @Id
    @Column(name = "\"verificationId\"")
    @GeneratedValue
    @UuidGenerator
    private UUID verificationId;

    @Column(name = "\"accountId\"", nullable = false, unique = true)
    private UUID accountId;

    @Column(name = "\"username\"", nullable = false)
    private String username;

    @Column(name = "\"employeeEmail\"", nullable = false)
    private String employeeEmail;

    @Column(name = "\"verificationCodeHash\"", nullable = false, length = 1200)
    private String verificationCodeHash;

    @Column(name = "\"expiresAt\"", nullable = false)
    private Timestamp expiresAt;

    @Column(name = "\"usedAt\"")
    private Timestamp usedAt;

    @CreationTimestamp
    @Column(name = "\"createdAt\"", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "\"updatedAt\"")
    private Timestamp updatedAt;
}