package com.aui.scholarship.controller.pub;


import com.aui.scholarship.model.request.MidtransNoficationRequest;
import com.aui.scholarship.service.MidtransService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pub/midtrans")
public class MidtransPublicController {
  private final MidtransService service;

  public MidtransPublicController(MidtransService service) {
    this.service = service;
  }

  @PostMapping("/notification")
  public void notification(@RequestBody(required = false) MidtransNoficationRequest request) {
    service.handleNotification(request);
  }

}
