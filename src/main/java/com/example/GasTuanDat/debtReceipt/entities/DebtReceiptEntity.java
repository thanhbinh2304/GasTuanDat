package com.example.GasTuanDat.debtReceipt.entities;

import java.time.LocalDate;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import com.example.GasTuanDat.customer.entities.CustomerEntity;
import com.example.GasTuanDat.gasBook.entities.GasBookEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "\"DebtReceipt\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DebtReceiptEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "\"receiptId\"", nullable = false)
    private UUID receiptId;

    @Column(name = "\"receiptCode\"", unique = true, nullable = false)
    private String receiptCode;

    @ManyToOne
    @JoinColumn(name = "\"customerId\"")
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "\"gasBookId\"")
    private GasBookEntity gasBook;

    @Column(name = "\"debtDate\"")
    private LocalDate debtDate;

    @Column(name = "\"dueDate\"")
    private LocalDate dueDate;

    @Column(name = "status")
    private String status; // "Chưa trả nợ" or "Đã trả nợ"

    @Column(name = "note", columnDefinition = "text")
    private String note;

    @OneToMany(mappedBy = "debtReceipt", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    private List<DebtReceiptDetailEntity> details = new ArrayList<>();
}
