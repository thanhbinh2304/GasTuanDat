package com.example.GasTuanDat.stockTransfer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.GasTuanDat.stockTransfer.entities.StockTransferEntity;
import com.example.GasTuanDat.stock.entities.StockEntity;

import java.util.UUID;

@Repository
public interface StockTransferRepository extends JpaRepository<StockTransferEntity, UUID> {
    boolean existsByTransferCodeIgnoreCase(String transferCode);

    @Query(value = """
        select st from StockTransferEntity st
        left join fetch st.fromStock fs
        left join fetch st.toStock ts
        left join fetch st.employee e
        where (coalesce(:keyword, '') = '' or st.transferCode ilike concat('%', :keyword, '%'))
            and (coalesce(:startDate, '') = '' or cast(st.transferDate as date) >= cast(:startDate as date))
            and (coalesce(:endDate, '') = '' or cast(st.transferDate as date) <= cast(:endDate as date))
            and (
                (:fromStockId is null and :fromStockName is null)
                or (
                    (:fromStockId is not null and fs.stockId = :fromStockId)
                    or (coalesce(:fromStockName, '') <> '' and fs.name ilike :fromStockName)
                )
            )
            and (
                (:toStockId is null and :toStockName is null)
                or (
                    (:toStockId is not null and ts.stockId = :toStockId)
                    or (coalesce(:toStockName, '') <> '' and ts.name ilike :toStockName)
                )
            )
            and (
                (:employeeId is null and :employeeName is null)
                or (
                    (:employeeId is not null and e.employeeId = :employeeId)
                    or (coalesce(:employeeName, '') <> '' and e.fullName ilike :employeeName)
                )
            )
        """, countQuery = """
        select count(st) from StockTransferEntity st
        left join st.fromStock fs
        left join st.toStock ts
        left join st.employee e
        where (coalesce(:keyword, '') = '' or st.transferCode ilike concat('%', :keyword, '%'))
            and (coalesce(:startDate, '') = '' or cast(st.transferDate as date) >= cast(:startDate as date))
            and (coalesce(:endDate, '') = '' or cast(st.transferDate as date) <= cast(:endDate as date))
            and (
                (:fromStockId is null and :fromStockName is null)
                or (
                    (:fromStockId is not null and fs.stockId = :fromStockId)
                    or (coalesce(:fromStockName, '') <> '' and fs.name ilike :fromStockName)
                )
            )
            and (
                (:toStockId is null and :toStockName is null)
                or (
                    (:toStockId is not null and ts.stockId = :toStockId)
                    or (coalesce(:toStockName, '') <> '' and ts.name ilike :toStockName)
                )
            )
            and (
                (:employeeId is null and :employeeName is null)
                or (
                    (:employeeId is not null and e.employeeId = :employeeId)
                    or (coalesce(:employeeName, '') <> '' and e.fullName ilike :employeeName)
                )
            )
        """)
    Page<StockTransferEntity> searchByFilters(
            @Param("keyword") String keyword,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("fromStockId") UUID fromStockId,
            @Param("fromStockName") String fromStockName,
            @Param("toStockId") UUID toStockId,
            @Param("toStockName") String toStockName,
            @Param("employeeId") UUID employeeId,
            @Param("employeeName") String employeeName,
            Pageable pageable);
}