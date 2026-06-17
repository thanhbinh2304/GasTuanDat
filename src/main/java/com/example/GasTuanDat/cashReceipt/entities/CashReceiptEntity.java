package com.example.GasTuanDat.cashReceipt.entities;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;

import com.example.GasTuanDat.customer.entities.CustomerEntity;
import com.example.GasTuanDat.customer.entities.ObjectEntity;
import com.example.GasTuanDat.employee.entities.EmployeeEntity;
import com.example.GasTuanDat.payment.enitites.TransactionTypeEntity;
import com.example.GasTuanDat.purchase.entities.PurchaseOrderEntity;
import com.example.GasTuanDat.sale.entities.SaleInvoiceEntity;
import com.example.GasTuanDat.supplier.entities.SupplierEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "\"CashReceipt\"")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashReceiptEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "\"receiptId\"", nullable = false)
    private UUID receiptId;
    
    @NotNull
    @Column(name = "\"receiptCode\"", nullable = false, unique = true)
    private String receiptCode;

    @NotNull    
    @ColumnDefault("(now() AT TIME ZONE 'utc'::text)")
    @Column(name = "\"receiptDate\"", nullable = false)
    private OffsetDateTime receiptDate;

    @Column(name = "\"receiptAmount\"")
    private BigDecimal receiptAmount;

    @Column(name = "\"PaymentMethod\"", columnDefinition = "paymentmethod")
    private Object paymentMethod;

    @Column(name = "note", length = Integer.MAX_VALUE)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @ColumnDefault("gen_random_uuid()")
    @JoinColumn(name = "\"createdBy\"")
    private EmployeeEntity createdBy;
    
    @Column(name = "\"createdDate\"")
    private OffsetDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @ColumnDefault("gen_random_uuid()")
    @JoinColumn(name = "\"employeeId\"")
    private EmployeeEntity employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @ColumnDefault("gen_random_uuid()")
    @JoinColumn(name = "\"customerId\"")
    private CustomerEntity customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @ColumnDefault("gen_random_uuid()")
    @JoinColumn(name = "\"supplierId\"")
    private SupplierEntity supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @ColumnDefault("gen_random_uuid()")
    @JoinColumn(name = "\"objectId\"")
    private ObjectEntity object;

    @ManyToOne(fetch = FetchType.LAZY)
    @ColumnDefault("gen_random_uuid()")
    @JoinColumn(name = "\"transactionTypeId\"")
    private TransactionTypeEntity transactionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @ColumnDefault("gen_random_uuid()")
    @JoinColumn(name = "\"invoiceId\"")
    private SaleInvoiceEntity invoice;
}
