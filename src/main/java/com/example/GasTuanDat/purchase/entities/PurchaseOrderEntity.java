package com.example.GasTuanDat.purchase.entities;


import com.example.GasTuanDat.employee.entities.EmployeeEntity;
import com.example.GasTuanDat.stock.entities.StockEntity;
import com.example.GasTuanDat.supplier.entities.SupplierEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Data
@Entity
@Table(name = "\"PurchaseOrder\"")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "\"purchaseId\"", nullable = false)
    private UUID purchaseId;

    @NotNull
    @Column(name = "\"purchaseCode\"", nullable = false, unique = true)
    private String purchaseCode;    

    @NotNull
    @ColumnDefault("(now() AT TIME ZONE 'utc'::text)")
    @Column(name = "\"purchaseDate\"", nullable = false)
    private OffsetDateTime purchaseDate;

    @Column(name = "\"totalAmount\"")
    private BigDecimal totalAmount;

    @Column(name = "\"discountAmount\"")
    private BigDecimal discountAmount;

    @Column(name = "\"paidAmount\"")
    private BigDecimal paidAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @ColumnDefault("gen_random_uuid()")
    @JoinColumn(name = "\"employeeId\"")
    private EmployeeEntity employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @ColumnDefault("gen_random_uuid()")
    @JoinColumn(name = "\"supplierId\"")
    private SupplierEntity supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @ColumnDefault("gen_random_uuid()")
    @JoinColumn(name = "\"stockId\"")
    private StockEntity stock;

    @Column(name = "note", length = Integer.MAX_VALUE)
    private String note;

    @Column(name = "\"orderType\"", columnDefinition = "ordertype")
    @org.hibernate.annotations.ColumnTransformer(read = "\"orderType\"::varchar", write = "?::ordertype")
    private String orderType;

   






}