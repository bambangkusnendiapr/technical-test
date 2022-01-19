package com.enigma.technicaltest.service;

import com.enigma.technicaltest.dto.CustomerDTO;
import com.enigma.technicaltest.dto.MerchantDTO;
import com.enigma.technicaltest.entity.Customer;
import com.enigma.technicaltest.entity.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface MerchantService {
  Page<Merchant> getAll (Pageable pageable, MerchantDTO merchantDTO, Sort sort);
  Merchant getById(String id);
  Merchant update ( String id,Merchant merchant);
  String delete (String id);
}
