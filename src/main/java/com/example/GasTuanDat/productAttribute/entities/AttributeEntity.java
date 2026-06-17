package com.example.GasTuanDat.productAttribute.entities;

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
@Table(name = "\"Attribute\"")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttributeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "\"attributeId\"", nullable = false)
    private UUID attributeId;

    @Column(name = "\"attributeName\"", nullable = false, columnDefinition = "text")
    private String attributeName;
}
