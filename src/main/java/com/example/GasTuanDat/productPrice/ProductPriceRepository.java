package com.example.GasTuanDat.productPrice;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.GasTuanDat.productPrice.entities.ProductPriceEntity;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPriceEntity, UUID> {
    @Query("""
            select pp from ProductPriceEntity pp
            join pp.product p
            join pp.priceList pl
                where (coalesce(:name, '') = ''
                    or lower(p.productName) like lower(concat('%', :name, '%'))
                    or lower(pl.priceListName) like lower(concat('%', :name, '%')))
            """)
    Page<ProductPriceEntity> searchByProductOrPriceListName(@Param("name") String name, Pageable pageable);

    List<ProductPriceEntity> findByProductProductId(UUID productId);
    void deleteByProductProductId(UUID productId);
}
