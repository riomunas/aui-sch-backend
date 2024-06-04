package com.aui.scholarship.model.request;

import com.aui.scholarship.model.Credential;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserRequest(
    String username,
    String firstName,
    String lastName,
    String email,
    String phoneNumber,
    String photoBase64,
    boolean emailVerified,
    boolean enabled,
    List<Credential> credentials
) {
  public KeycloakUserRequest toKeyCloakUserRequest() {
    return new KeycloakUserRequest(
        username,
        firstName,
        lastName,
        email,
        emailVerified,
        true,
        credentials
    );
  }
}
