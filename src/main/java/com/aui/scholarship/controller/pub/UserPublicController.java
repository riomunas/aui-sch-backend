package com.aui.scholarship.controller.pub;

import com.aui.scholarship.model.request.LoginRequest;
import com.aui.scholarship.model.request.RegisterRequest;
import com.aui.scholarship.model.response.ResponseDto;
import com.aui.scholarship.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import static com.aui.scholarship.model.Status.FAILED;

@RestController
@RequestMapping("/pub/user")
public class UserPublicController {
  private final Logger log = LoggerFactory.getLogger(UserPublicController.class);
  private final UserService userService;

  public UserPublicController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
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

  @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseDto> register(@RequestBody RegisterRequest request) {
    log.info(">> register request: {}", request);
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

  @GetMapping(value = "/hi", produces = MediaType.APPLICATION_JSON_VALUE)
  public String hi() {
    return "Hi";
  }
}
