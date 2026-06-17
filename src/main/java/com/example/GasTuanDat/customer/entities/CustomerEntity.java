package com.example.GasTuanDat.customer.entities;

import com.example.GasTuanDat.customer.entities.ObjectEntity;
import com.example.GasTuanDat.ward.entities.WardEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
@Data
@Entity
@Table(name = "\"Customer\"")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "\"customerId\"", nullable = false)
    private UUID customerId;

    @Column(name = "\"customerCode\"", unique = true)
    private String customerCode;    

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
    @JoinColumn(name = "\"customerGroupId\"")
    private CustomerGroupEntity customerGroup;



}