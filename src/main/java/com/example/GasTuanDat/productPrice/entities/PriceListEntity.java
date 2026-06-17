package com.example.GasTuanDat.productPrice.entities;

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
@Data
@Table(name = "\"PriceList\"")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "\"priceListId\"", nullable = false)
    private UUID priceListId;

    @Column(name = "\"priceListName\"", nullable = false, columnDefinition = "text")
    private String priceListName;
}
