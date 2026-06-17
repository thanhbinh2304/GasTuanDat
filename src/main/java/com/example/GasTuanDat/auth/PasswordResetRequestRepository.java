package com.example.GasTuanDat.auth;

import com.example.GasTuanDat.auth.entity.PasswordResetRequestEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetRequestRepository extends JpaRepository<PasswordResetRequestEntity, UUID> {
}
