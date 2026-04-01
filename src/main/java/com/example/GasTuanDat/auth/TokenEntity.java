package com.example.GasTuanDat.auth;

import java.sql.Timestamp;
import java.util.UUID;

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
@Table(name = "\"Token\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenEntity {
    @Id
    @Column(name = "\"tokenId\"")
    @GeneratedValue
    @UuidGenerator
    private UUID tokenId;

    @Column(name = "\"accountId\"")
    private UUID accountId;

    @Column(name = "\"refreshToken\"", length = 1200, nullable = false)
    private String refreshToken;

    @Column(name = "\"expiresAt\"")
    private Timestamp expiresAt;

    @Column(name = "\"createdAt\"")
    private Timestamp createdAt;
}