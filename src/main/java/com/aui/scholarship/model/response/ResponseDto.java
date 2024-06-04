package com.aui.scholarship.model.response;

import com.aui.scholarship.model.Status;

public record ResponseDto(Status status, Object data) {

  public ResponseDto(Object data) {
    this(Status.SUCCESS, data);
  }
}

