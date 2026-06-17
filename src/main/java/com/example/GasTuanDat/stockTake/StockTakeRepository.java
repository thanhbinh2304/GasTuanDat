package com.example.GasTuanDat.stockTake;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.GasTuanDat.stockTake.entities.StockTakeEntity;
import java.util.UUID;
import java.time.OffsetDateTime;

@Repository
public interface StockTakeRepository extends JpaRepository<StockTakeEntity, UUID> {
    boolean existsByStockTakeCodeIgnoreCase(String stockTakeCode);

    @Query(value = """
        select st from StockTakeEntity st
        left join fetch st.stock s
        left join fetch st.employee e
        where (coalesce(:keyword, '') = '' or st.stockTakeCode ilike concat('%', :keyword, '%'))
            and (cast(:startDate as timestamp) is null or st.stockTakeDate >= :startDate)
            and (cast(:endDate as timestamp) is null or st.stockTakeDate <= :endDate)
            and (
                (:stockId is null and :stockName is null)
                or (
                    (:stockId is not null and s.stockId = :stockId)
                    or (coalesce(:stockName, '') <> '' and s.name ilike :stockName)
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
        select count(st) from StockTakeEntity st
        left join st.stock s
        left join st.employee e
        where (coalesce(:keyword, '') = '' or st.stockTakeCode ilike concat('%', :keyword, '%'))
            and (cast(:startDate as timestamp) is null or st.stockTakeDate >= :startDate)
            and (cast(:endDate as timestamp) is null or st.stockTakeDate <= :endDate)
            and (
                (:stockId is null and :stockName is null)
                or (
                    (:stockId is not null and s.stockId = :stockId)
                    or (coalesce(:stockName, '') <> '' and s.name ilike :stockName)
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
    Page<StockTakeEntity> searchByFilters(
            @Param("keyword") String keyword,
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate,
            @Param("stockId") UUID stockId,
            @Param("stockName") String stockName,
            @Param("employeeId") UUID employeeId,
            @Param("employeeName") String employeeName,
            Pageable pageable
    );
}
