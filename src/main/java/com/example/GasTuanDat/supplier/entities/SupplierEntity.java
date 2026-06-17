package com.example.GasTuanDat.supplier.entities;

import java.sql.Timestamp;
import java.util.UUID;
import java.math.BigDecimal;

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
@Table(name = "\"Supplier\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierEntity {
    @Id
    @Column(name = "\"supplierId\"")
    @GeneratedValue
    @UuidGenerator
    private UUID supplierId;

    @Column(name = "\"fullName\"")
    private String fullName;

    @Column(name = "\"phoneNumber\"")
    private String phoneNumber;

    @Column(name = "\"email\"")
    private String email;

    @Column(name = "\"note\"", columnDefinition = "TEXT")
    private String note;

    @Column(name = "\"wardId\"")
    private UUID wardId;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "debt")
    private BigDecimal debt = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(name = "\"createdAt\"", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "\"updatedAt\"")
    private Timestamp updatedAt;
}