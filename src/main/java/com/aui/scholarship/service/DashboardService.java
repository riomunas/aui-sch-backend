package com.aui.scholarship.service;

import com.aui.scholarship.model.repository.PaketRepository;
import com.aui.scholarship.model.repository.TransactionRepository;
import com.aui.scholarship.model.repository.UserRepository;
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
public class DashboardService {
  private final Logger log = LoggerFactory.getLogger(DashboardService.class);
  private final PaketRepository paketRepository;
  private final UserRepository userRepository;
  private final TransactionRepository transactionRepository;

  public DashboardService(PaketRepository repository, UserRepository userRepository, TransactionRepository transactionRepository) {
    this.paketRepository = repository;
    this.userRepository = userRepository;
    this.transactionRepository = transactionRepository;
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
    List<MyPaketInfo> myPaketInfo = paketRepository.findAllMyPaketForDashboard(userId);
    for (MyPaketInfo paketInfo : myPaketInfo) {
      totalAset = totalAset.add(paketInfo.getCurrentPrice());
    }
    result.put("user", user);
    result.put("total_aset", totalAset);
    result.put("paket", myPaketInfo);
    return result;
  }
}
