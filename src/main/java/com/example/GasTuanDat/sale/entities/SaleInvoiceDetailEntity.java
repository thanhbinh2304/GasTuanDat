package com.example.GasTuanDat.sale.entities;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.GasTuanDat.product.entities.ProductEntity;
import com.example.GasTuanDat.purchase.entities.PurchaseOrderEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "\"InvoiceDetail\"",
       uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_saleinvoice_product",
            columnNames = {"invoiceId", "productId"}
        )
       }
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleInvoiceDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "\"id\"", nullable = false)
    private UUID id;

    @JoinColumn(name = "\"invoiceId\"", nullable = false)
    @ManyToOne
    private SaleInvoiceEntity invoice;

    @JoinColumn(name = "\"productId\"", nullable = false)
    @ManyToOne
    private ProductEntity product;

    @Column(name = "\"quantity\"", nullable = false)
    private Integer quantity;

    @Column(name = "\"unitPrice\"", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "\"total\"", nullable = false)
    private BigDecimal total;
}
