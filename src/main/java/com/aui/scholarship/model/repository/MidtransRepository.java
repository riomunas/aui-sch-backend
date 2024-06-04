package com.aui.scholarship.model.repository;

import com.aui.scholarship.model.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MidtransRepository extends JpaRepository<TransactionEntity, String> {
}
