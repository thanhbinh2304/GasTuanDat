package com.example.GasTuanDat.productAttribute;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.GasTuanDat.productAttribute.entities.AttributeEntity;

@Repository
public interface AttributeRepository extends JpaRepository<AttributeEntity, UUID> {
    Optional<AttributeEntity> findByAttributeNameIgnoreCase(String attributeName);

    @Query("""
            select a from AttributeEntity a
            where (coalesce(:attributeName, '') = '' or lower(a.attributeName) like lower(concat('%', :attributeName, '%')))
            """)
    Page<AttributeEntity> searchByAttributeName(@Param("attributeName") String attributeName, Pageable pageable);
}
