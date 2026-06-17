package com.example.GasTuanDat.stockTransfer.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;

import com.example.GasTuanDat.employee.entities.EmployeeEntity;
import com.example.GasTuanDat.stock.entities.StockEntity;

@Data
@Entity
@Table(name = "\"StockTransfer\"")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockTransferEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "\"transferId\"", nullable = false)
    private UUID transferId;

    @NotNull
    @Column(name = "\"transferCode\"", nullable = false, unique = true)
    private String transferCode;

    @NotNull
    @ColumnDefault("(now() AT TIME ZONE 'utc'::text)")
    @Column(name = "\"transferDate\"", nullable = false)
    private OffsetDateTime transferDate;

    @Column(name = "note", length = Integer.MAX_VALUE)
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @ColumnDefault("gen_random_uuid()")
    @JoinColumn(name = "\"employeeId\"")
    private EmployeeEntity employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @ColumnDefault("gen_random_uuid()")
    @JoinColumn(name = "\"fromStockId\"")
    private StockEntity fromStock;

    @ManyToOne(fetch = FetchType.LAZY)
    @ColumnDefault("gen_random_uuid()")
    @JoinColumn(name = "\"toStockId\"")
    private StockEntity toStock;

    @OneToMany(mappedBy = "transfer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.BatchSize(size = 50)
    private List<StockTransferDetailEntity> details;
}
