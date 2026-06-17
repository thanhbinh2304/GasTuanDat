package com.example.GasTuanDat.stockTake.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;

import com.example.GasTuanDat.employee.entities.EmployeeEntity;
import com.example.GasTuanDat.stock.entities.StockEntity;

@Data
@Entity
@Table(name = "\"StockTake\"")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockTakeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "\"stockTakeId\"", nullable = false)
    private UUID stockTakeId;

    @NotNull
    @Column(name = "\"stockTakeCode\"", nullable = false, unique = true)
    private String stockTakeCode;

    @NotNull
    @Column(name = "\"stockTakeDate\"", nullable = false)
    private OffsetDateTime stockTakeDate;

    @Column(name = "\"note\"", columnDefinition = "text")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @ColumnDefault("gen_random_uuid()")
    @JoinColumn(name = "\"employeeId\"")
    private EmployeeEntity employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @ColumnDefault("gen_random_uuid()")
    @JoinColumn(name = "\"stockId\"")
    private StockEntity stock;
}
