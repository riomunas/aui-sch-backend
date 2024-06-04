package com.aui.scholarship.model.repository;

import com.aui.scholarship.model.entity.ClaimEntity;
import com.aui.scholarship.model.entity.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClaimRepository extends JpaRepository<ClaimEntity, UUID> {
}
