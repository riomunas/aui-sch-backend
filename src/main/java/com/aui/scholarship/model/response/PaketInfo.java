package com.aui.scholarship.model.response;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaketInfo {
  UUID getId();
  String getName();
  String getDescription();
  BigDecimal getPrice();
}
