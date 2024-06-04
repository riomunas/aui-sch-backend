package com.aui.scholarship.service;

import com.aui.scholarship.model.entity.ClaimEntity;
import com.aui.scholarship.model.entity.TransferEntity;
import com.aui.scholarship.model.repository.*;
import com.aui.scholarship.model.response.MyPaketInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TransactionService {
  private final Logger log = LoggerFactory.getLogger(TransactionService.class);
  private final PaketRepository paketRepository;
  private final UserRepository userRepository;
  private final TransactionRepository transactionRepository;
  private final TransferRepository transferRepository;
  private final ClaimRepository claimRepository;

  public TransactionService(PaketRepository repository, UserRepository userRepository, TransactionRepository transactionRepository,
                            TransferRepository transferRepository,
                            ClaimRepository claimRepository) {
    this.paketRepository = repository;
    this.userRepository = userRepository;
    this.transactionRepository = transactionRepository;
    this.transferRepository = transferRepository;
    this.claimRepository = claimRepository;
  }

  public Object getData(UUID userId) {
    HashMap<String, Object> result = new HashMap<>();

    //data user
    var user = userRepository.findById(userId).orElse(null);
    if (user == null) {
      throw new RuntimeException("User not found");
    }

    //data sumary asset
    var totalAset = BigDecimal.ZERO;
    //data paket
    List<MyPaketInfo> myPaketInfo = paketRepository.findAllMyPaket(userId);
    for (MyPaketInfo paketInfo : myPaketInfo) {
      totalAset = totalAset.add(paketInfo.getCurrentPrice());
    }
    result.put("user", user);
    result.put("total_aset", totalAset);
    result.put("paket", paketRepository.findAllMyPaket(userId));
    return result;
  }

  public Object getDataTransfer(String orderId, String userIdTujuan) {
    var user = userRepository.findById(UUID.fromString(userIdTujuan)).orElse(null);
    if (user == null) {
      throw new RuntimeException("User not found");
    }

    var paket = paketRepository.findMyPaketByOrderId(UUID.fromString(orderId)).orElse(null);
    if (paket == null) {
      throw new RuntimeException("Paket not found");
    }
    log.info("user {}", user);
    log.info("paket {}", paket);

    HashMap<String, Object> result = new HashMap<>();
    result.put("paket", paket);
    result.put("user", user);
    return result;
  }

  public void transfer(String orderId, String userIdTujuan, UUID userId) {
    var user = userRepository.findById(UUID.fromString(userIdTujuan)).orElse(null);
    if (user == null) {
      throw new RuntimeException("User not found");
    }
    var paket = paketRepository.findMyPaketByOrderId(UUID.fromString(orderId)).orElse(null);
    if (paket == null) {
      throw new RuntimeException("Paket not found");
    }

    transferRepository.save(new TransferEntity(UUID.randomUUID(), orderId, userId, userIdTujuan));
    transactionRepository.findByOrderId(UUID.fromString(orderId)).ifPresent(transaction -> {
      transaction.setUserId(UUID.fromString(userIdTujuan));
      transactionRepository.save(transaction);
    });
  }

  public void claim(String orderId, UUID userId) {
    var transaction = transactionRepository.findByOrderId(UUID.fromString(orderId)).orElse(null);
    if (transaction == null) {
      throw new RuntimeException("Transaction not found");
    }

    claimRepository.save(new ClaimEntity(UUID.randomUUID(), transaction.getOrderId(), "claimed"));
  }
}
