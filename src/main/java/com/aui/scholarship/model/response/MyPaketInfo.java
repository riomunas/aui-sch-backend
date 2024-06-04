package com.aui.scholarship.model.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface MyPaketInfo {
  UUID getId();
  String getName();
  BigDecimal getStartPrice();
  BigDecimal getCurrentPrice();
  String getDate();
  String getClaimId();
}
