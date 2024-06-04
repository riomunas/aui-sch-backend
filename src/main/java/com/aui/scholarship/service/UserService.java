package com.aui.scholarship.service;

import com.aui.scholarship.config.KeycloakConfig;
import com.aui.scholarship.model.entity.UserEntity;
import com.aui.scholarship.model.repository.UserRepository;
import com.aui.scholarship.model.request.*;
import com.aui.scholarship.model.response.AccessTokenResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Transactional
public class UserService {
  private final Logger log = LoggerFactory.getLogger(UserService.class);
  private final RestClient restClient;
  private final KeycloakConfig keycloakConfig;
  private final UserRepository repository;
  private final ImageKitService imageKitService;

  public UserService(KeycloakConfig keycloakConfig, UserRepository repository, ImageKitService imageKitService) {
    this.keycloakConfig = keycloakConfig;
    this.restClient = RestClient.builder().baseUrl(keycloakConfig.getBaseUrl()).build();
    this.repository = repository;
    this.imageKitService = imageKitService;
  }

  public AccessTokenResponse login(LoginRequest request) {
    return restClient.post()
        .uri("/realms/aui-scholarship/protocol/openid-connect/token")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .body(toRequestLoginBody(request))
        .retrieve().body(AccessTokenResponse.class);
  }

  public AccessTokenResponse refreshToken(RefreshTokenRequest request) {
    return restClient.post()
        .uri("/realms/aui-scholarship/protocol/openid-connect/token")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .body(toRequestRefreshTokenBody(request))
        .retrieve().body(AccessTokenResponse.class);
  }

  public AccessTokenResponse logout(LogoutRequest request) {
    return restClient.post()
        .uri("/realms/aui-scholarship/protocol/openid-connect/logout")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .body(toRequestLogoutBody(request))
        .retrieve().body(AccessTokenResponse.class);
  }

  public AccessTokenResponse getToken() {
    return restClient.post()
        .uri("/realms/aui-scholarship/protocol/openid-connect/token")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .body(toRequestTokenBody())
        .retrieve().body(AccessTokenResponse.class);
  }

  public UserEntity register(RegisterRequest request) throws JsonProcessingException {
    record ErrorMessage(String errorMessage) {}

    var token = getToken();
    AtomicReference<UserEntity> user = new AtomicReference<>(new UserEntity(request.username(), request.firstName(), request.lastName(), request.email()));
    //create keycloak user
    restClient.post()
        .uri("admin/realms/aui-scholarship/users")
        .contentType(MediaType.APPLICATION_JSON)
        .body(request.toKeyCloakUserRegisterRequest())
        .headers(headers -> headers.setBearerAuth(token.accessToken()))
        .exchange((req, res) -> {
          ErrorMessage em = res.bodyTo(new ParameterizedTypeReference<ErrorMessage>() {});
          if (res.getStatusCode().is2xxSuccessful()) {
            var createdUsers = restClient.get()
                .uri("admin/realms/aui-scholarship/users?email={email}", request.email())
                .headers(headers -> headers.setBearerAuth(token.accessToken()))
                .retrieve().body(new ParameterizedTypeReference<List<UserEntity>>() {});

            assert createdUsers != null;
            user.get().setId(createdUsers.getFirst().getId());

            try {
              var photoUrl = imageKitService.generatePhotoUrl(new ImageKitUploadReqeust(request.photoBase64(), createdUsers.getFirst().getId().toString(), "users"));
              user.get().setPhotoUrl(photoUrl);
            } catch (Exception e) {
              log.error("Failed to upload image: {}", e.getMessage());
            }

            return repository.save(user.get());
          } else {
            throw new RuntimeException(em.errorMessage);
          }
        });
        return user.get();
  }


  public UserEntity update(UUID userId, UserRequest request) {
    var token = getToken();
    ResponseEntity<Void> res = restClient.put()
        .uri("admin/realms/aui-scholarship/users/{id}", userId)
        .contentType(MediaType.APPLICATION_JSON)
        .body(request.toKeyCloakUserRequest())
        .headers(headers -> headers.setBearerAuth(token.accessToken()))
        .retrieve()
        .toBodilessEntity();

    if (res.getStatusCode().is2xxSuccessful()) {
      var user = repository.findById(userId).orElse(null);
      user.setUsername(request.username());
      user.setFirstName(request.firstName());
      user.setLastName(request.lastName());
      user.setEmail(request.email());
      user.setPhoneNumber(request.phoneNumber());

      //kalau kirim photo
      if (request.photoBase64() != null) {
        try {
          var photoUrl = imageKitService.generatePhotoUrl(new ImageKitUploadReqeust(request.photoBase64(), user.getId().toString(), "users"));
          user.setPhotoUrl(photoUrl);
        } catch (Exception e) {
          log.error("Failed to upload image: {}", e.getMessage());
        }
      }

      return repository.save(user);
    } else {
      throw new RuntimeException(res.getStatusCode().toString());
    }
  }

  public MultiValueMap<String, String> toRequestLoginBody(LoginRequest request) {
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "password");
    body.add("client_id", keycloakConfig.getClientId());
    body.add("client_secret", keycloakConfig.getClientSecret());
    body.add("username", request.username());
    body.add("password", request.password());
    return body;
  }

  public MultiValueMap<String, String> toRequestRefreshTokenBody(RefreshTokenRequest request) {
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "refresh_token");
    body.add("client_id", keycloakConfig.getClientId());
    body.add("client_secret", keycloakConfig.getClientSecret());
    body.add("refresh_token", request.refreshToken());
    return body;
  }

  public MultiValueMap<String, String> toRequestLogoutBody(LogoutRequest request) {
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("client_id", keycloakConfig.getClientId());
    body.add("client_secret", keycloakConfig.getClientSecret());
    body.add("refresh_token", request.refreshToken());
    return body;
  }
  public MultiValueMap<String, String> toRequestTokenBody() {
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("client_id", keycloakConfig.getClientId());
    body.add("client_secret", keycloakConfig.getClientSecret());
    body.add("grant_type", "client_credentials");
    return body;
  }

  public Object me(UUID userId) {
    return repository.findById(userId).orElse(null);
  }

  public Object updatePassword(UUID userId, UbahPasswordRequest request) {
    var token = getToken();
    ResponseEntity<Void> res = restClient.put()
        .uri("admin/realms/aui-scholarship/users/{id}", userId)
        .contentType(MediaType.APPLICATION_JSON)
        .body(request)
        .headers(headers -> headers.setBearerAuth(token.accessToken()))
        .retrieve()
        .toBodilessEntity();

    if (res.getStatusCode().is2xxSuccessful()) {
      return repository.findById(userId).orElse(null);
    } else {
      throw new RuntimeException(res.getStatusCode().toString());
    }
  }
}
