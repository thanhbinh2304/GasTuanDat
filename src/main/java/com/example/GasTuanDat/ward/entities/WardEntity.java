package com.example.GasTuanDat.ward.entities;

import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;

import com.example.GasTuanDat.employee.entities.EmployeeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"Ward\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "\"wardId\"", nullable = false)
    private UUID wardId;
    
    @Column(name = "\"wardName\"", nullable = false)
    private String wardName;

    @ManyToOne(fetch = FetchType.EAGER)
    @ColumnDefault("gen_random_uuid()")
    @JoinColumn(name = "\"areaId\"")
    private AreaEntity area;

}
