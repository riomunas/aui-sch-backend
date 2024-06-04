package com.aui.scholarship.model.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AccessTokenResponse(
    String accessToken,
    long expiresIn,
    long refreshExpiresIn,
    String refreshToken,
    String tokenType,
    int notBeforePolicy,
    String sessionState,
    String scope
) {}
