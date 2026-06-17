package com.example.GasTuanDat.debtReceipt.entities;

import java.util.UUID;
import java.math.BigDecimal;

import com.example.GasTuanDat.product.entities.ProductEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"DebtReceiptDetail\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DebtReceiptDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "\"receiptId\"")
    private DebtReceiptEntity debtReceipt;

    @ManyToOne
    @JoinColumn(name = "\"productId\"")
    private ProductEntity product;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "\"priceList\"")
    private String priceList;
}
