package com.example.GasTuanDat.employee;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.GasTuanDat.employee.entities.EmployeeEntity;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, UUID> {
    Optional<EmployeeEntity> findByEmployeeCode(String employeeCode);

    @Query("""
        select e from EmployeeEntity e
        where (:keyword is null or :keyword = ''
            or lower(e.employeeCode) like lower(concat('%', :keyword, '%'))
            or lower(e.fullName) like lower(concat('%', :keyword, '%'))
            or e.phoneNumber like concat('%', :keyword, '%'))
        and (:jobTitle is null or :jobTitle = '' or lower(e.position.name) = lower(:jobTitle))
        order by e.fullName asc""")
    Page<EmployeeEntity> searchEmployees(
            @Param("keyword") String keyword,
            @Param("jobTitle") String jobTitle,
            Pageable pageable);
}
