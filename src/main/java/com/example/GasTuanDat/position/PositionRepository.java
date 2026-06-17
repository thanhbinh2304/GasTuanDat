package com.example.GasTuanDat.position;

import com.example.GasTuanDat.position.entities.PositionEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<PositionEntity, UUID> {
    Optional<PositionEntity> findByName(String name);
}
