package com.example.GasTuanDat.customer;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GasTuanDat.customer.entities.CustomerGroupEntity;

@Repository
public interface CustomerGroupRepository extends JpaRepository<CustomerGroupEntity, UUID> {
}
