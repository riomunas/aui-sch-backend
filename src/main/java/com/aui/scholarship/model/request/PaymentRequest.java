package com.aui.scholarship.model.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PaymentRequest(TransactionDetail transactionDetails, List<ItemDetailRequest> itemDetails, CustomerDetailRequest customerDetails){}