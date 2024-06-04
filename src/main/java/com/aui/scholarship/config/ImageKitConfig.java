package com.aui.scholarship.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

@Configuration
@ConfigurationProperties(prefix = "imagekit")
public class ImageKitConfig {
  private String publicKey;
  private String privateKey;
  private String urlEndpoint;
  private String urlUploadEndpoint;

  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  public String getPrivateKey() {
    return privateKey;
  }

  public void setPrivateKey(String privateKey) {
    this.privateKey = privateKey;
  }

  public String getUrlEndpoint() {
    return urlEndpoint;
  }

  public void setUrlEndpoint(String urlEndpoint) {
    this.urlEndpoint = urlEndpoint;
  }

  public String getUrlUploadEndpoint() {
    return urlUploadEndpoint;
  }

  public void setUrlUploadEndpoint(String urlUploadEndpoint) {
    this.urlUploadEndpoint = urlUploadEndpoint;
  }

  public String getAuthorization() {
    return Base64.getEncoder().encodeToString((publicKey + ":" + privateKey).getBytes());
  }
}
