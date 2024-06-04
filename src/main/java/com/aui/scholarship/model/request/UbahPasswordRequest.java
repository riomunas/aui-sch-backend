package com.aui.scholarship.model.request;

import com.aui.scholarship.model.Credential;

import java.util.List;

public record UbahPasswordRequest(List<Credential> credentials) { }
