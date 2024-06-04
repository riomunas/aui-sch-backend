package com.aui.scholarship.controller.api;

import com.aui.scholarship.model.response.ResponseDto;
import com.aui.scholarship.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.aui.scholarship.model.Status.FAILED;

@RestController
@RequestMapping(value = "/api/transaction/", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {
  private final TransactionService service;

  public TransactionController(TransactionService service) {
    this.service = service;
  }

  @GetMapping(value = "/transfer/{orderId}/{userIdTujuan}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseDto> getDataTransfer(@PathVariable(value = "orderId") String orderId,
                                                          @PathVariable(value = "userIdTujuan") String userIdTujuan) {
    try {
      return ResponseEntity.ok(new ResponseDto(service.getDataTransfer(orderId, userIdTujuan)));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
          new ResponseDto(FAILED, e.getMessage())
      );
    }
  }

  @PostMapping(value = "/transfer/{orderId}/{userIdTujuan}", produces = MediaType.APPLICATION_JSON_VALUE)
  public void transfer(@PathVariable(value = "orderId") String orderId,
                                              @PathVariable(value = "userIdTujuan") String userIdTujuan,
                                              @RequestHeader(name = "User-Id", required = false) UUID userId) {
    try {
      service.transfer(orderId, userIdTujuan, userId);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  @PostMapping(value = "/claim/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public void claim(@PathVariable(value = "orderId") String orderId,
                                              @RequestHeader(name = "User-Id", required = false) UUID userId) {
    try {
      service.claim(orderId, userId);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
