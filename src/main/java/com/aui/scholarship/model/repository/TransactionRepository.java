package com.aui.scholarship.model.repository;

import com.aui.scholarship.model.entity.TransactionEntity;
import com.aui.scholarship.model.response.MyPaketInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {
  List<TransactionEntity> findByUserId(UUID userId);

  @Query(value = """
      SELECT * FROM transactions
      WHERE user_id = ?1 AND paket_id = ?2 AND (status_code = '201' or status_code is null)
      AND deleted_at is null
      LIMIT 1
    """, nativeQuery = true)
  Optional<TransactionEntity> getExistingCheckout(UUID userId, UUID paketId);

  Optional<TransactionEntity> findByOrderId(UUID uuid);
}
