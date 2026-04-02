package com.example.GasTuanDat.profile;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;
@Entity
@Table(name = "\"Profile\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileEntity {
    @Id
    @Column(name = "\"profileId\"")
    @GeneratedValue
    @UuidGenerator
    private UUID profileId;

    @Column(name = "\"accountId\"")
    private UUID accountId;

    @Column(name = "\"fullName\"")
    private String fullName;

    @Column(name = "\"phoneNumber\"")
    private String phoneNumber;

    @Column(name = "\"dateOfBirth\"")
    private LocalDate dateOfBirth;

    @Column(name = "\"gender\"")
    private Boolean gender;

    @Column(name = "\"avatar\"")
    private  String avatar;

    @Column(name = "\"address\"")
    private  String address;

     @CreationTimestamp
    @Column(name = "\"createdAt\"", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "\"updatedAt\"")
    private Timestamp updatedAt;

    @Column(name = "\"deleteAt\"")
    private LocalDateTime deleteAt;
}
