package com.enigma.technicaltest.service;

import com.enigma.technicaltest.dto.TransferDTO;
import com.enigma.technicaltest.entity.Customer;
import com.enigma.technicaltest.entity.Transfer;
import com.enigma.technicaltest.request.FillInBalanceRequest;
import com.enigma.technicaltest.request.TransferRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface TransferService {
  Page<Transfer> getAll(Pageable pageable, TransferDTO transferDTO);
  Transfer fillInBalance(FillInBalanceRequest request);
  Transfer transfer(TransferRequest request);
}
