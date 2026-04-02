package com.example.GasTuanDat.department;

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
@Table(name = "\"Department\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentEntity {
    @Id
    @Column(name = "\"departmentId\"")
    @GeneratedValue
    @UuidGenerator
    private UUID departmentId;

    @Column(name = "\"name\"")
    private String name;
}
