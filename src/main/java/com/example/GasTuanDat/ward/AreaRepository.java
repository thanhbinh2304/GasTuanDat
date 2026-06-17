package com.example.GasTuanDat.ward;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.GasTuanDat.ward.entities.AreaEntity;

@Repository
public interface AreaRepository extends JpaRepository<AreaEntity, UUID> {
}
