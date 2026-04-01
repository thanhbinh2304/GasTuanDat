package com.example.GasTuanDat.role;

import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"Role\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleEntity {
    @Id
    @Column(name = "\"roleId\"")
    @GeneratedValue
    @UuidGenerator
    private UUID roleId;

    @Column(name = "\"roleName\"")
    private String roleName;

    @Column(name = "\"description\"")
    private String description;

    @CreationTimestamp
    @Column(name = "\"createdAt\"", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "\"updatedAt\"")
    private Timestamp updatedAt;

}
