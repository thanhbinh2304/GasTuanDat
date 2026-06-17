package com.example.GasTuanDat.customer.entities;


import com.example.GasTuanDat.ward.entities.WardEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.UUID;
@Data
@Entity
@Table(name = "\"Object\"")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ObjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "\"objectId\"", nullable = false)
    private UUID objectId;

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

    @ManyToOne
    @JoinColumn(name = "\"wardId\"")
    private WardEntity ward;
}