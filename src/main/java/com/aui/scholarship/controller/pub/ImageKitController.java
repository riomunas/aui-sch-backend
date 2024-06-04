package com.aui.scholarship.controller.pub;


import com.aui.scholarship.model.request.ImageKitUploadReqeust;
import com.aui.scholarship.model.response.ResponseDto;
import com.aui.scholarship.service.ImageKitService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/pub/imagekit")
public class ImageKitController {
  private static final Logger log = LoggerFactory.getLogger(ImageKitController.class);
  private final ImageKitService imageKitService;

  public ImageKitController(ImageKitService imageKitService) {
    this.imageKitService = imageKitService;
  }

  @PostMapping(value="/upload", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseDto> upload(@RequestBody ImageKitUploadReqeust request) throws JsonProcessingException {
    return ResponseEntity.ok().body(new ResponseDto(imageKitService.generatePhotoUrl(request)));
  }
}
