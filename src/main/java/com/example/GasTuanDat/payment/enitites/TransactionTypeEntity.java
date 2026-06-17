package com.example.GasTuanDat.payment.enitites;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"TransactionType\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "\"transactionTypeId\"", nullable = false)
    private UUID transactionTypeId;

    @Column(name = "\"transactionTypeName\"", nullable = false)
    private String transactionTypeName;
}
