package com.aui.scholarship.service;


import com.aui.scholarship.config.MidtransConfig;
import com.aui.scholarship.model.entity.TransactionEntity;
import com.aui.scholarship.model.repository.MidtransRepository;
import com.aui.scholarship.model.repository.PaketRepository;
import com.aui.scholarship.model.repository.TransactionRepository;
import com.aui.scholarship.model.repository.UserRepository;
import com.aui.scholarship.model.request.*;
import com.aui.scholarship.model.response.SnapResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MidtransService {
  private static final Logger log = LoggerFactory.getLogger(MidtransService.class);
  private final UserRepository userRepository;
  private final MidtransRepository repository;
  private final MidtransConfig midtransConfig;
  private final PaketRepository paketRepository;
  private final RestClient restClientApp;
  private final RestClient restClientApi;
  private final TransactionRepository transactionRepository;

  public MidtransService(MidtransRepository repository, MidtransConfig midtransConfig, PaketRepository paketRepository, TransactionRepository transactionRepository,
                         UserRepository userRepository) {
    this.repository = repository;
    this.midtransConfig = midtransConfig;
    this.paketRepository = paketRepository;
    this.restClientApp = RestClient.builder().baseUrl(midtransConfig.getAppBaseUrl()).build();
    this.restClientApi = RestClient.builder().baseUrl(midtransConfig.getApiBaseUrl()).build();
    this.transactionRepository = transactionRepository;
    this.userRepository = userRepository;
  }


  public void handleNotification(MidtransNoficationRequest request) {
    try {
      String sha512Hash = Sha512DigestUtils.shaHex(request.orderId()+request.statusCode()+request.grossAmount()+midtransConfig.getServerKey());
      // periksi ini bener dari midtrans apa ndak
      if (sha512Hash.equals(request.signatureKey())) {
        // ambil data transaction berdasarkan order_id yang datang update transaction status sesuai dengan data yang masuk
        var transaction = transactionRepository.findById(UUID.fromString(request.orderId())).orElse(null);
        if (transaction != null) {
          transaction.setStatusCode(request.statusCode());
          transaction.setTransactionStatus(request.transactionStatus());
          transaction.setTransactionTime(request.transactionTime());
          transaction.setSettlementTime(request.settlementTime());
          transaction.setUpdatedAt(transaction.getCreatedAt());
          log.info(">> transaction updated : {}", transaction);
          transactionRepository.save(transaction);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  public SnapResponse createTransaction(UUID userId, UUID paketId) throws JsonProcessingException {
    String snapResponse = null;

    var user = userRepository.findById(userId).orElse(null);
    if (user == null) throw new RuntimeException("User not found");

    //cari paket
    var paket = paketRepository.findPaketById(paketId).orElse(null);
    if (paket == null) throw new RuntimeException("Paket not found");

    //cek apakah ada transaksi dengan user & paket & status null kalau ada return aja itu
    var transaction = transactionRepository.getExistingCheckout(userId, paketId).orElse(null);

    //transaksi baru
    var orderId = UUID.randomUUID();
    if (transaction == null) {
      PaymentRequest paymentRequest = new PaymentRequest(
          new TransactionDetail(orderId, paket.getPrice()),
          List.of(new ItemDetailRequest(paket.getId().toString(), paket.getPrice(), 1, paket.getName())),
          new CustomerDetailRequest(user.getFirstName(), user.getLastName(), user.getEmail())
      );

      //cari data snap
      snapResponse = restClientApp.post()
          .uri("snap/v1/transactions")
          .contentType(MediaType.APPLICATION_JSON)
          .body(paymentRequest)
          .headers(headers -> headers.setBasicAuth(midtransConfig.getSnapAuthorization()))
          .retrieve().body(String.class);

      //simpan order
      transactionRepository.save(new TransactionEntity(orderId, paket.getPrice(), userId, paketId, snapResponse));
    } else {
      orderId = transaction.getOrderId();
      //transaksi existing
      snapResponse = transaction.getSnapResponse();
    }

    SnapResponse response = new ObjectMapper().readValue(snapResponse, SnapResponse.class);
    return new SnapResponse(response.token(), response.redirectUrl(), orderId.toString());
  }

  public void cancelOrder(String orderId) {
    transactionRepository.findByOrderId(UUID.fromString(orderId))
        .ifPresent(transaction -> {
          //kalau pending baru bisa cancel
          if (transaction.getStatusCode() != null && transaction.getStatusCode().equals("201")) {
            restClientApi.post()
                .uri("/" + orderId + "/cancel")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(headers -> headers.setBasicAuth(midtransConfig.getSnapAuthorization()))
                .retrieve().toBodilessEntity();

            transaction.setStatusCode("202");
            transaction.setTransactionStatus("cancel");
            transaction.setUpdatedAt(OffsetDateTime.now());
            transactionRepository.save(transaction);
          }
        });
  }
}
