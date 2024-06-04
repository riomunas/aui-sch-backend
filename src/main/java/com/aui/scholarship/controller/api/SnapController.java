package com.aui.scholarship.controller.api;

import com.aui.scholarship.config.MidtransConfig;
import com.aui.scholarship.model.response.ResponseDto;
import com.aui.scholarship.model.request.PaymentRequest;
import com.aui.scholarship.model.response.SnapResponse;
import com.aui.scholarship.service.MidtransService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.UUID;

import static com.aui.scholarship.model.Status.FAILED;

@RestController
@RequestMapping(value = "/api/snap",  produces = MediaType.APPLICATION_JSON_VALUE)
public class SnapController {
  private static final Logger log = LoggerFactory.getLogger(SnapController.class);
  private final RestClient restClient;
  private final MidtransConfig midtransConfig;
  private final MidtransService service;

  public SnapController(MidtransConfig midtransConfig, MidtransService service) {
    this.restClient = RestClient.builder().baseUrl(midtransConfig.getAppBaseUrl()).build();
    this.midtransConfig = midtransConfig;
    this.service = service;
  }

  @PostMapping(value = "/checkout/{paketId}",  produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseDto> checkout(
      @PathVariable(value = "paketId") UUID paketId,
      @RequestHeader(name = "User-Id", required = true) UUID userId
  ) {
    try {
      return ResponseEntity.ok(new ResponseDto(service.createTransaction(userId, paketId)));
    } catch (HttpClientErrorException e) {
      e.printStackTrace();
      return ResponseEntity.status(e.getStatusCode()).body(new ResponseDto(FAILED, e.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
          new ResponseDto(FAILED, e.getMessage())
      );
    }
  }

  @PostMapping( value = "/redirect-url",  produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseDto> redirectUrl(@RequestBody PaymentRequest request) {
    log.info(">> login request: {}", request);
    try {
      var snapUrl = restClient.post()
          .uri("snap/v1/transactions")
          .contentType(MediaType.APPLICATION_JSON)
          .body(request)
          .headers(headers -> headers.setBasicAuth( midtransConfig.getSnapAuthorization()))
          .retrieve().body(SnapResponse.class);
      log.info(">> snap url: {}", snapUrl);
      return ResponseEntity.ok(new ResponseDto(snapUrl));
    } catch (HttpClientErrorException e) {
      e.printStackTrace();
      return ResponseEntity.status(e.getStatusCode()).body(new ResponseDto(FAILED, e.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
          new ResponseDto(FAILED, e.getMessage())
      );
    }
  }
}
