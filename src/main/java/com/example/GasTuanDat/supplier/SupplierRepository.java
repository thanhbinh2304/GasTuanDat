package com.example.GasTuanDat.supplier;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.GasTuanDat.supplier.entities.SupplierEntity;

@Repository
public interface SupplierRepository extends JpaRepository<SupplierEntity, UUID> {
    /**
     * Find supplier by phone number
     */
    Optional<SupplierEntity> findByPhoneNumber(String phoneNumber);

    /**
     * Search suppliers by name or phone with pagination
     * Supports flexible search with null parameters
     */
    @Query("""
        select s from SupplierEntity s
        where coalesce(:keyword, '') = '' 
        or lower(s.fullName) like lower(concat('%', :keyword, '%'))
        or s.phoneNumber like concat('%', :keyword, '%')
        or lower(cast(s.supplierId as string)) like lower(concat('%', :keyword, '%'))
        or lower(concat('ncc', cast(s.supplierId as string))) like lower(concat('%', :keyword, '%'))
        order by s.fullName asc""")
    Page<SupplierEntity> searchSuppliers(
            @Param("keyword") String keyword,
            Pageable pageable);
}
