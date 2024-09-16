package com.aui.scholarship.controller.api;

import com.aui.scholarship.model.request.*;
import com.aui.scholarship.model.response.ResponseDto;
import com.aui.scholarship.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.UUID;

import static com.aui.scholarship.model.Status.FAILED;

@RestController
@RequestMapping("/api/user")
public class UserController {
  Logger log = LoggerFactory.getLogger(UserController.class);
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  public ResponseEntity<ResponseDto> login(@RequestBody LoginRequest request) {
    log.info(">> login request: {}", request);
    try {
      return ResponseEntity.ok(new ResponseDto(userService.login(request)));
    } catch (HttpClientErrorException e) {
      return ResponseEntity.status(e.getStatusCode()).body(new ResponseDto(FAILED, e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
          new ResponseDto(FAILED, e.getMessage())
      );
    }
  }

  @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseDto> me(@RequestHeader(name = "User-Id", required = true) UUID userId) {
    try {
      return ResponseEntity.ok(new ResponseDto(userService.me(userId)));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
          new ResponseDto(FAILED, e.getMessage())
      );
    }
  }

  @PostMapping(value = "/refresh-token", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseDto> refreshToken(@RequestBody RefreshTokenRequest request) {
    log.info(">> refresh token request: {}", request);
    try {
      return ResponseEntity.ok(new ResponseDto(userService.refreshToken(request)));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
          new ResponseDto(FAILED, "Failed to refresh token")
      );
    }
  }

  @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseDto> refreshToken(@RequestBody LogoutRequest request) {
    log.info(">> logout  request: {}", request);
    try {
      return ResponseEntity.ok(new ResponseDto(userService.logout(request)));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
          new ResponseDto(FAILED, "Failed to refresh token")
      );
    }
  }


  public ResponseEntity<ResponseDto> register(@RequestBody RegisterRequest request) {
    try {
      return ResponseEntity.ok(new ResponseDto(userService.register(request)));
    } catch (Exception e) {
      e.printStackTrace();
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
          new ResponseDto(FAILED, e.getMessage())
      );
    }
  }

  @PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseDto> update(@RequestBody UserRequest request,
                                            @RequestHeader(name = "User-Id", required = true) UUID userId) {
    try {
      return ResponseEntity.ok(new ResponseDto(userService.update(userId, request)));
    } catch (Exception e) {
      e.printStackTrace();
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
          new ResponseDto(FAILED, e.getMessage())
      );
    }
  }

  @PostMapping(value = "/update-password", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseDto> updatePassword(@RequestBody UbahPasswordRequest request,
                                            @RequestHeader(name = "User-Id", required = true) UUID userId) {
    try {
      return ResponseEntity.ok(new ResponseDto(userService.updatePassword(userId, request)));
    } catch (Exception e) {
      e.printStackTrace();
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
          new ResponseDto(FAILED, e.getMessage())
      );
    }
  }

}
