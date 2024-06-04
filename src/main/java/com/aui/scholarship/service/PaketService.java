package com.aui.scholarship.service;

import com.aui.scholarship.model.repository.PaketRepository;
import com.aui.scholarship.model.repository.TransactionRepository;
import com.aui.scholarship.model.response.MyPaketInfo;
import com.aui.scholarship.model.response.PaketInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PaketService {
  private final PaketRepository repository;
  private final TransactionRepository transactionRepository;

  public PaketService(PaketRepository repository, TransactionRepository transactionRepository) {
    this.repository = repository;
    this.transactionRepository = transactionRepository;
  }

  public List<PaketInfo> findAllPakets() {
    return repository.findAllPaket();
  }


  public List<MyPaketInfo> findMyAllPaket(UUID userId) {
    return repository.findAllMyPaket(userId);
  }

  public MyPaketInfo findMyPaketByOrderId(String orderId) {
    return repository.findMyPaketByOrderId(UUID.fromString(orderId)).orElse(null);
  }

  public List<MyPaketInfo> findAllMyReadyToClaimPaket(UUID userId) {
    return repository.findAllMyReadyToClaimPaket(userId);
  }
}
