package com.example.GasTuanDat.auth;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GasTuanDat.auth.entity.PasswordChangeVerificationEntity;

@Repository
public interface PasswordChangeVerificationRepository extends JpaRepository<PasswordChangeVerificationEntity, UUID> {
    Optional<PasswordChangeVerificationEntity> findByAccountId(UUID accountId);
}