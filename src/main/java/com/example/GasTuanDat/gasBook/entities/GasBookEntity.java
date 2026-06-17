package com.example.GasTuanDat.gasBook.entities;

import java.time.LocalDate;
import java.util.UUID;
import java.math.BigDecimal;

import com.example.GasTuanDat.customer.entities.CustomerGroupEntity;
import com.example.GasTuanDat.ward.entities.WardEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "\"GasBook\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GasBookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "\"gasBookId\"", nullable = false)
    private UUID gasBookId;

    @Column(name = "\"gasBookCode\"", unique = true)
    private String gasBookCode;

    @Column(name = "\"fullName\"", nullable = false)
    private String fullName;

    @Column(name="\"email\"", unique = true)
    private String email;

    @Column(name="\"phoneNumber\"")
    private String phoneNumber;

    @Column(name = "gender")
    private Boolean gender;

    @Column(name = "\"dateOfBirth\"")
    private LocalDate dateOfBirth;

    @Column(name = "note", length = Integer.MAX_VALUE)
    private String note;

    @Column(name = "address", length = Integer.MAX_VALUE)
    private String address;

    @Column(name = "debt")
    private BigDecimal debt = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "\"wardId\"")
    private WardEntity ward;

    @ManyToOne
    @JoinColumn(name = "\"customerGroupid\"")
    private CustomerGroupEntity customerGroup;

    @Column(name = "points")
    private Integer points;

    @Column(name = "cycle")
    private Integer cycle;

}
