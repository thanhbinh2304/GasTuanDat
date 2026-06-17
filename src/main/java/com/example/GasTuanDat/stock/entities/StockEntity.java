package com.example.GasTuanDat.stock.entities;

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
@Table(name = "\"Stock\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "\"stockId\"", nullable = false)
    private UUID stockId;

    @Column(name = "\"name\"", nullable = true, columnDefinition = "text")
    private String name;

    @Column(name = "\"wardId\"", nullable = true)
    private UUID wardId;
}
