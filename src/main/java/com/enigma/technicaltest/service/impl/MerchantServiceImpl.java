package com.enigma.technicaltest.service.impl;

import com.enigma.technicaltest.dto.CustomerDTO;
import com.enigma.technicaltest.dto.MerchantDTO;
import com.enigma.technicaltest.entity.Customer;
import com.enigma.technicaltest.entity.Merchant;
import com.enigma.technicaltest.exception.NotFoundException;
import com.enigma.technicaltest.repository.MerchantRepository;
import com.enigma.technicaltest.service.MerchantService;
import com.enigma.technicaltest.specification.MerchantSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MerchantServiceImpl implements MerchantService {

  @Autowired
  private MerchantRepository merchantRepository;

  @Override
  public Page<Merchant> getAll(Pageable pageable, MerchantDTO merchantDTO, Sort sort) {
    Specification<Merchant> specification = MerchantSpecification.getSpecification(merchantDTO);
    return merchantRepository.findAll(specification, pageable);
  }

  @Override
  public Merchant getById(String id) {
    return findByIdOrThrowNotFound(id);
  }

  @Override
  public Merchant update(String id, Merchant merchant) {
    Merchant updateMerchant = findByIdOrThrowNotFound(id);
    updateMerchant.setName(merchant.getName());
    updateMerchant.setAddress(merchant.getAddress());
    return merchantRepository.save(updateMerchant);
  }

  @Override
  public String delete(String id) {
    Merchant merchant = findByIdOrThrowNotFound(id);
    if (merchant.getIsDeleted()) {
      throw new NotFoundException("Merchant not found");
    } else {
      merchant.setIsDeleted(true);
      merchantRepository.save(merchant);
    }
    return "Merchant has been deleted";
  }

  private Merchant findByIdOrThrowNotFound(String id) {
    Optional<Merchant> merchant = merchantRepository.findById(id);
    if (merchant.isPresent()) {
      return merchant.get();
    } else {
      throw new NotFoundException("Merchant not found");
    }
  }
}
