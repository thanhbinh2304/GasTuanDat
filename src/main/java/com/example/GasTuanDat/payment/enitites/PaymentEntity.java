package com.example.GasTuanDat.payment.enitites;

import com.example.GasTuanDat.purchase.entities.PurchaseOrderEntity;
import com.example.GasTuanDat.stock.entities.StockEntity;
import com.example.GasTuanDat.supplier.entities.SupplierEntity;
import com.example.GasTuanDat.customer.entities.CustomerEntity;
import com.example.GasTuanDat.customer.entities.ObjectEntity;
import com.example.GasTuanDat.employee.entities.EmployeeEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.ColumnDefault;

@Data
@Entity
@Table(name = "\"Payment\"")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "\"paymentId\"", nullable = false)
    private UUID paymentId;

    @NotNull
    @Column(name = "\"paymentCode\"", nullable = false, unique = true)
    private String paymentCode;

    @NotNull
    @ColumnDefault("(now() AT TIME ZONE 'utc'::text)")
    @Column(name = "\"paymentDate\"", nullable = false)
    private OffsetDateTime paymentDate;

    @Column(name = "\"paymentAmount\"")
    private BigDecimal paymentAmount;

    @Column(name = "\"paymentMethod\"", columnDefinition = "paymentmethod")
    private Object paymentMethod;

    @Column(name = "notes", length = Integer.MAX_VALUE)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @ColumnDefault("gen_random_uuid()")
    @JoinColumn(name = "\"createdBy\"")
    private EmployeeEntity createdBy;


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
    @JoinColumn(name = "\"purchaseId\"")
    private PurchaseOrderEntity purchase;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @ColumnDefault("gen_random_uuid()")
    @JoinColumn(name = "\"stockId\"")
    private StockEntity stock;

}