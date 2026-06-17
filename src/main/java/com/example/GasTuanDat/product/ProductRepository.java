package com.example.GasTuanDat.product;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.GasTuanDat.product.entities.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    boolean existsByProductNameIgnoreCase(String productName);

    boolean existsByProductCodeIgnoreCase(String productCode);

    @Query("SELECT p.productCode FROM ProductEntity p WHERE p.productCode LIKE concat(:prefix, '%')")
    java.util.List<String> findProductCodesByPrefix(@Param("prefix") String prefix);

    Page<ProductEntity> findByProductNameContainingIgnoreCase(String productName, Pageable pageable);

    @Query(value = """
            select distinct p from ProductEntity p
            where (coalesce(:keyword, '') = '' or p.productName ilike concat('%', :keyword, '%') or p.productCode ilike concat('%', :keyword, '%'))
                and (coalesce(:productCategory, '') = '' or p.category.categoryName ilike :productCategory)
                  and (
                      (:stockId is null and :stockName is null)
                      or exists (
                          select 1
                          from com.example.GasTuanDat.stock.entities.InventoryEntity i
                          join i.stock s
                          where i.product = p
                           and (
                                (:stockId is not null and s.stockId = :stockId)
                                or (coalesce(:stockName, '') <> '' and s.name ilike :stockName)
                              )
                      )
                  )
                  and (
                      (:priceListId is null and :priceListName is null)
                      or exists (
                          select 1
                          from com.example.GasTuanDat.productPrice.entities.ProductPriceEntity pp
                          join pp.priceList pl
                          where pp.product = p
                           and (
                                (:priceListId is not null and pl.priceListId = :priceListId)
                                or (coalesce(:priceListName, '') <> '' and pl.priceListName ilike :priceListName)
                              )
                      )
                  )
                  and (
                      (:productAttributeId is null and coalesce(:productAttributeName, '') = '' and coalesce(:attributeValue, '') = '')
                      or exists (
                          select 1
                          from com.example.GasTuanDat.productAttribute.entities.ProductAttributeEntity pa
                          join pa.attribute a
                          where pa.product = p
                           and (
                                (:productAttributeId is null and coalesce(:productAttributeName, '') = '')
                                or (:productAttributeId is not null and a.attributeId = :productAttributeId)
                                or (coalesce(:productAttributeName, '') <> '' and a.attributeName ilike :productAttributeName)
                           )
                           and (coalesce(:attributeValue, '') = '' or lower(pa.attributeValue) like lower(concat('%', :attributeValue, '%')))
                      )
                  )
            """, countQuery = """
            select count(distinct p) from ProductEntity p
                where (coalesce(:keyword, '') = '' or p.productName ilike concat('%', :keyword, '%') or p.productCode ilike concat('%', :keyword, '%'))
                    and (coalesce(:productCategory, '') = '' or p.category.categoryName ilike :productCategory)
                  and (
                      (:stockId is null and :stockName is null)
                      or exists (
                          select 1
                          from com.example.GasTuanDat.stock.entities.InventoryEntity i
                          join i.stock s
                          where i.product = p
                           and (
                                (:stockId is not null and s.stockId = :stockId)
                                or (coalesce(:stockName, '') <> '' and s.name ilike :stockName)
                              )
                      )
                  )
                  and (
                      (:priceListId is null and :priceListName is null)
                      or exists (
                          select 1
                          from com.example.GasTuanDat.productPrice.entities.ProductPriceEntity pp
                          join pp.priceList pl
                          where pp.product = p
                           and (
                                (:priceListId is not null and pl.priceListId = :priceListId)
                                or (coalesce(:priceListName, '') <> '' and pl.priceListName ilike :priceListName)
                              )
                      )
                  )
                  and (
                      (:productAttributeId is null and coalesce(:productAttributeName, '') = '' and coalesce(:attributeValue, '') = '')
                      or exists (
                          select 1
                          from com.example.GasTuanDat.productAttribute.entities.ProductAttributeEntity pa
                          join pa.attribute a
                          where pa.product = p
                           and (
                                (:productAttributeId is null and coalesce(:productAttributeName, '') = '')
                                or (:productAttributeId is not null and a.attributeId = :productAttributeId)
                                or (coalesce(:productAttributeName, '') <> '' and a.attributeName ilike :productAttributeName)
                           )
                           and (coalesce(:attributeValue, '') = '' or lower(pa.attributeValue) like lower(concat('%', :attributeValue, '%')))
                      )
                  )
            """)
    Page<ProductEntity> searchByFilters(
            @Param("keyword") String keyword,
            @Param("productCategory") String productCategory,
            @Param("stockId") UUID stockId,
            @Param("stockName") String stockName,
            @Param("priceListId") UUID priceListId,
            @Param("priceListName") String priceListName,
            @Param("productAttributeId") UUID productAttributeId,
            @Param("productAttributeName") String productAttributeName,
            @Param("attributeValue") String attributeValue,
            Pageable pageable);
}
