package com.example.GasTuanDat.productPrice;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.GasTuanDat.productPrice.entities.PriceListEntity;

@Repository
public interface PriceListRepository extends JpaRepository<PriceListEntity, UUID> {
    Optional<PriceListEntity> findByPriceListNameIgnoreCase(String priceListName);

    @Query("""
            select p from PriceListEntity p
            where (coalesce(:name, '') = '' or lower(p.priceListName) like lower(concat('%', :name, '%')))
            """)
    Page<PriceListEntity> searchByName(@Param("name") String name, Pageable pageable);
}