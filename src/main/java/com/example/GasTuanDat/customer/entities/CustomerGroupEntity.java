package com.example.GasTuanDat.customer.entities;

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
@Table(name = "\"CustomerGroup\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "\"customerGroupid\"", nullable = false)
    private UUID customerGroupId;

    @Column(name="\"groupName\"", nullable = false)
    private String groupName;
}
