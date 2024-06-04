package com.aui.scholarship.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "pakets")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaketEntity {

  public PaketEntity() {
    this.createdAt = OffsetDateTime.now();
  }

  @Id
  private UUID id;
  private String name;
  private String description;
  private Integer defaultLevel;
  private Integer claimAllowedYear;
  @CreationTimestamp
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getDefaultLevel() {
    return defaultLevel;
  }

  public void setDefaultLevel(Integer defaultLevel) {
    this.defaultLevel = defaultLevel;
  }

  public Integer getClaimAllowedYear() {
    return claimAllowedYear;
  }

  public void setClaimAllowedYear(Integer claimAllowedYear) {
    this.claimAllowedYear = claimAllowedYear;
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
}
