package com.aui.scholarship.model.request;

import com.aui.scholarship.model.Credential;

import java.util.List;

public record RegisterRequest(
    String username,
    String firstName,
    String lastName,
    String email,
    boolean emailVerified,
    boolean enabled,
    List<Credential> credentials,
    String photoBase64
) {
  public KeycloakUserRequest toKeyCloakUserRegisterRequest() {
    return new KeycloakUserRequest(
        username,
        firstName,
        lastName,
        email,
        emailVerified,
        enabled,
        credentials
    );
  }
}
