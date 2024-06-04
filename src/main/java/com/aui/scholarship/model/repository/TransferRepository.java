package com.aui.scholarship.model.repository;

import com.aui.scholarship.model.entity.TransferEntity;
import com.aui.scholarship.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransferRepository extends JpaRepository<TransferEntity, UUID> {
}
