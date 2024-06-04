package com.aui.scholarship.controller.api;

import com.aui.scholarship.model.response.ResponseDto;
import com.aui.scholarship.service.DashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.UUID;

import static com.aui.scholarship.model.Status.FAILED;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
  private final DashboardService service;

  public DashboardController(DashboardService service) {
    this.service = service;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseDto> login(@RequestHeader(name = "User-Id", required = true) UUID userId) {
    try {
      return ResponseEntity.ok(new ResponseDto(service.getData(userId)));
    } catch (HttpClientErrorException e) {
      return ResponseEntity.status(e.getStatusCode()).body(new ResponseDto(FAILED, e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
          new ResponseDto(FAILED, e.getMessage())
      );
    }
  }
}
