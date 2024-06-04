package com.aui.scholarship.model.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MidtransNoficationRequest(String statusCode, String signatureKey, String orderId, String grossAmount, String transactionStatus, String transactionTime, String settlementTime) {
}
