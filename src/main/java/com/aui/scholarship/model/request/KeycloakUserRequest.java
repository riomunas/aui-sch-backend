package com.aui.scholarship.model.request;

import com.aui.scholarship.model.Credential;

import java.util.List;

public record KeycloakUserRequest(
    String username,
    String firstName,
    String lastName,
    String email,
    boolean emailVerified,
    boolean enabled,
    List<Credential> credentials
) { }
