package com.example.GasTuanDat.sale.entities;

import com.example.GasTuanDat.customer.entities.CustomerEntity;
import com.example.GasTuanDat.employee.entities.EmployeeEntity;
import com.example.GasTuanDat.stock.entities.StockEntity;
import com.example.GasTuanDat.gasBook.entities.GasBookEntity;
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
@Table(name = "\"SaleInvoice\"")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleInvoiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "\"invoiceId\"", nullable = false)
    private UUID invoiceId;

    @NotNull
    @Column(name = "\"invoiceCode\"", nullable = false, unique = true)
    private String invoiceCode;

    @NotNull
    @ColumnDefault("(now() AT TIME ZONE 'utc'::text)")
    @Column(name = "\"invoiceDate\"", nullable = false)
    private OffsetDateTime invoiceDate;

    @Column(name = "\"totalAmount\"")
    private BigDecimal totalAmount;

    @Column(name = "\"discountAmount\"")
    private BigDecimal discountAmount;

    @Column(name = "\"paidAmount\"")
    private BigDecimal paidAmount;

    @Column(name = "\"orderType\"", columnDefinition = "ordertype")
    @org.hibernate.annotations.ColumnTransformer(read = "\"orderType\"::varchar", write = "?::ordertype")
    private String orderType;

    @Column(name = "note", length = Integer.MAX_VALUE)
    private String note;

    @Column(name = "\"PaymentMethod\"", columnDefinition = "paymentmethod")
    @org.hibernate.annotations.ColumnTransformer(read = "\"PaymentMethod\"::varchar", write = "?::paymentmethod")
    private String paymentMethod;

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
    @JoinColumn(name = "\"gasBookId\"")
    private GasBookEntity gasBook;

    @ManyToOne(fetch = FetchType.LAZY)
    @ColumnDefault("gen_random_uuid()")
    @JoinColumn(name = "\"stockId\"")
    private StockEntity stock;

    

}