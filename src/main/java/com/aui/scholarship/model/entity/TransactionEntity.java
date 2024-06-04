package com.aui.scholarship.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;


@Entity
@Table(name = "transactions")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionEntity {

  @Id
  private UUID orderId;
  private BigDecimal grossAmount;
  private String statusCode;
  private String transactionStatus;
  private String transactionTime;
  private String settlementTime;

  private UUID userId;
  private UUID paketId;
  private String snapResponse;
  @CreationTimestamp
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
  private OffsetDateTime deletedAt;

  public TransactionEntity() {
    this.createdAt = OffsetDateTime.now();
  }

  public TransactionEntity(UUID orderId, BigDecimal price, UUID userId, UUID paketId, String snapResponse) {
    this.orderId = orderId;
    this.grossAmount = price;
    this.userId = userId;
    this.paketId = paketId;
    this.snapResponse = snapResponse;
  }

  public OffsetDateTime getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(OffsetDateTime deletedAt) {
    this.deletedAt = deletedAt;
  }

  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public String getSnapResponse() {
    return snapResponse;
  }

  public void setSnapResponse(String snapResponse) {
    this.snapResponse = snapResponse;
  }

  public UUID getPaketId() {
    return paketId;
  }

  public void setPaketId(UUID paketId) {
    this.paketId = paketId;
  }

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public String getSettlementTime() {
    return settlementTime;
  }

  public void setSettlementTime(String settlementTime) {
    this.settlementTime = settlementTime;
  }

  public String getTransactionTime() {
    return transactionTime;
  }

  public void setTransactionTime(String transactionTime) {
    this.transactionTime = transactionTime;
  }

  public String getTransactionStatus() {
    return transactionStatus;
  }

  public void setTransactionStatus(String transactionStatus) {
    this.transactionStatus = transactionStatus;
  }

  public String getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(String statusCode) {
    this.statusCode = statusCode;
  }

  public BigDecimal getGrossAmount() {
    return grossAmount;
  }

  public void setGrossAmount(BigDecimal grossAmount) {
    this.grossAmount = grossAmount;
  }

  public UUID getOrderId() {
    return orderId;
  }

  public void setOrderId(UUID orderId) {
    this.orderId = orderId;
  }
}
