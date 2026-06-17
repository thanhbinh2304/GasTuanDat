package com.example.GasTuanDat.productAttribute;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.GasTuanDat.productAttribute.entities.ProductAttributeEntity;

@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttributeEntity, UUID> {
        @Query("""
                        select pa from ProductAttributeEntity pa
                        where (coalesce(:attributeValue, '') = '' or lower(pa.attributeValue) like lower(concat('%', :attributeValue, '%')))
                        """)
        Page<ProductAttributeEntity> searchByAttributeValue(@Param("attributeValue") String attributeValue,
                        Pageable pageable);

        @Query("""
                        select pa from ProductAttributeEntity pa
                        where pa.attribute.attributeId = :attributeId and pa.product.productId = :productId
                        """)
        Optional<ProductAttributeEntity> findByAttributeIdAndProductId(@Param("attributeId") UUID attributeId,
                        @Param("productId") UUID productId);

        List<ProductAttributeEntity> findByProductProductId(UUID productId);
        void deleteByProductProductId(UUID productId);
}
