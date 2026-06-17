package com.example.GasTuanDat.gasBook;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.GasTuanDat.gasBook.entities.RewardMilestoneEntity;

@Repository
public interface RewardMilestoneRepository extends JpaRepository<RewardMilestoneEntity, UUID>, JpaSpecificationExecutor<RewardMilestoneEntity> {
    boolean existsByPromotionCode(String promotionCode);
}
