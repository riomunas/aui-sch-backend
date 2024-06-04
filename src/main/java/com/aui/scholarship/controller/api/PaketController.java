package com.aui.scholarship.controller.api;

import com.aui.scholarship.model.response.ResponseDto;
import com.aui.scholarship.service.PaketService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.aui.scholarship.model.Status.FAILED;

@RestController
@RequestMapping(value = "/api/paket", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaketController {
  private final Logger log = LoggerFactory.getLogger(PaketController.class);
  private final PaketService service;

  public PaketController(PaketService service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<ResponseDto> findAllPakets() {
    try {
      return ResponseEntity.ok(new ResponseDto(service.findAllPakets()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
          new ResponseDto(FAILED, e.getMessage())
      );
    }
  }

  @GetMapping(value = "/my-paket", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseDto> findMyAllPaket(@RequestHeader(name = "User-Id", required = true) UUID userId) {
    try {
      return ResponseEntity.ok(new ResponseDto(service.findMyAllPaket(userId)));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
          new ResponseDto(FAILED, e.getMessage())
      );
    }
  }

  @GetMapping(value = "/my-paket-claim", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseDto> findAllMyReadyToClaimPaket(@RequestHeader(name = "User-Id", required = true) UUID userId) {
    try {
      return ResponseEntity.ok(new ResponseDto(service.findAllMyReadyToClaimPaket(userId)));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
          new ResponseDto(FAILED, e.getMessage())
      );
    }
  }

  @GetMapping(value = "/my-paket/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseDto> findMyAllPaket(@RequestHeader(name = "User-Id", required = true) UUID userId,
                                                     @PathVariable("orderId") String orderId) {
    try {
      return ResponseEntity.ok(new ResponseDto(service.findMyPaketByOrderId(orderId)));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
          new ResponseDto(FAILED, e.getMessage())
      );
    }
  }

}
