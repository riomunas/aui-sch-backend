package com.aui.scholarship.service;


import com.aui.scholarship.config.ImageKitConfig;
import com.aui.scholarship.config.MidtransConfig;
import com.aui.scholarship.model.entity.TransactionEntity;
import com.aui.scholarship.model.repository.MidtransRepository;
import com.aui.scholarship.model.repository.PaketRepository;
import com.aui.scholarship.model.repository.TransactionRepository;
import com.aui.scholarship.model.repository.UserRepository;
import com.aui.scholarship.model.request.*;
import com.aui.scholarship.model.response.ImageKitUploadResponse;
import com.aui.scholarship.model.response.SnapResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.time.OffsetDateTime;
import java.util.*;

@Service
public class ImageKitService {
  private static final Logger log = LoggerFactory.getLogger(ImageKitService.class);
  private final RestClient restClient;
  private final ImageKitConfig config;

  public ImageKitService(ImageKitConfig config) {
    this.config = config;
    this.restClient = RestClient.builder().baseUrl(config.getUrlUploadEndpoint()).build();
  }

  public String generatePhotoUrl(ImageKitUploadReqeust request) {
    try {
      MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
      form.add("file", request.file());
      form.add("fileName", request.fileName());
      form.add("folder", request.folder());

      var res = restClient.post()
          .contentType(MediaType.MULTIPART_FORM_DATA)
          .body(form)
          .headers(headers -> {
            headers.setBasicAuth(Base64.getEncoder().encodeToString((config.getPrivateKey()+":").getBytes()));
          }).retrieve().toEntity(ImageKitUploadResponse.class);
      log.info(Objects.requireNonNull(res.getBody()).toString());
      return Objects.requireNonNull(res.getBody()).url();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
