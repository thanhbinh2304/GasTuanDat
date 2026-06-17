package com.example.GasTuanDat.customer;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.GasTuanDat.customer.entities.CustomerEntity;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {

    boolean existsByCustomerCodeIgnoreCase(String customerCode);
    java.util.Optional<CustomerEntity> findByCustomerCodeIgnoreCase(String customerCode);

    @Query("SELECT c.customerCode FROM CustomerEntity c WHERE c.customerCode LIKE concat(:prefix, '%')")
    java.util.List<String> findCustomerCodesByPrefix(@Param("prefix") String prefix);

    @Query("""
            select c from CustomerEntity c
            where (cast(:keyword as string) is null or lower(c.fullName) like lower(concat('%', cast(:keyword as string), '%')) or c.phoneNumber like concat('%', cast(:keyword as string), '%') or lower(c.customerCode) like lower(concat('%', cast(:keyword as string), '%')))
              and (:gender is null or c.gender = :gender)
              and (:dateOfBirth is null or c.dateOfBirth = :dateOfBirth)
              and (cast(:address as string) is null or lower(coalesce(c.ward.wardName, '')) like lower(concat('%', cast(:address as string), '%')))
              and (cast(:customerGroup as string) is null or lower(coalesce(c.customerGroup.groupName, '')) like lower(concat('%', cast(:customerGroup as string), '%')))
              and (:hasLastTransactionFrom = false or (
            	  select max(s.invoiceDate)
            	  from SaleInvoiceEntity s
            	  where s.customer = c
              ) >= :lastTransactionFrom)
              and (:hasLastTransactionTo = false or (
            	  select max(s.invoiceDate)
            	  from SaleInvoiceEntity s
            	  where s.customer = c
              ) < :lastTransactionTo)
            """)
    Page<CustomerEntity> searchCustomers(
            @Param("keyword") String keyword,
            @Param("gender") Boolean gender,
            @Param("dateOfBirth") LocalDate dateOfBirth,
            @Param("address") String address,
            @Param("customerGroup") String customerGroup,
            @Param("hasLastTransactionFrom") boolean hasLastTransactionFrom,
            @Param("lastTransactionFrom") OffsetDateTime lastTransactionFrom,
            @Param("hasLastTransactionTo") boolean hasLastTransactionTo,
            @Param("lastTransactionTo") OffsetDateTime lastTransactionTo,
            Pageable pageable);

    @Query("""
            select max(s.invoiceDate)
            from SaleInvoiceEntity s
            where s.customer.customerId = :customerId
            """)
    OffsetDateTime findLastTransactionDateByCustomerId(@Param("customerId") UUID customerId);
}
