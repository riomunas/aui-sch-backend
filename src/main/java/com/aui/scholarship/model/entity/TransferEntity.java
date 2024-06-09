package com.aui.scholarship.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;


@Entity
@Table(name = "transfers")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferEntity {

  @Id
  private UUID id;
  private UUID orderId;
  private UUID fromUserId;
  private UUID toUserId;
  private String status;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
  private OffsetDateTime deletedAt;

  public TransferEntity() {
    this.createdAt = OffsetDateTime.now();
  }

  public TransferEntity(UUID uuid, String orderId, UUID userId, String userIdTujuan) {
    this.id = uuid;
    this.orderId = UUID.fromString(orderId);
    this.fromUserId = userId;
    this.toUserId = UUID.fromString(userIdTujuan);
    this.status = "ok";
    this.createdAt = OffsetDateTime.now();
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getOrderId() {
    return orderId;
  }

  public void setOrderId(UUID orderId) {
    this.orderId = orderId;
  }

  public UUID getFromUserId() {
    return fromUserId;
  }

  public void setFromUserId(UUID fromUserId) {
    this.fromUserId = fromUserId;
  }

  public UUID getToUserId() {
    return toUserId;
  }

  public void setToUserId(UUID toUserId) {
    this.toUserId = toUserId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public OffsetDateTime getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(OffsetDateTime deletedAt) {
    this.deletedAt = deletedAt;
  }
}
