package com.enigma.technicaltest.service.impl;

import com.enigma.technicaltest.dto.CustomerDTO;
import com.enigma.technicaltest.dto.MerchantDTO;
import com.enigma.technicaltest.entity.Customer;
import com.enigma.technicaltest.entity.Merchant;
import com.enigma.technicaltest.entity.User;
import com.enigma.technicaltest.exception.BadRequestException;
import com.enigma.technicaltest.exception.NotFoundException;
import com.enigma.technicaltest.repository.MerchantRepository;
import com.enigma.technicaltest.repository.UserRepository;
import com.enigma.technicaltest.service.MerchantService;
import com.enigma.technicaltest.specification.MerchantSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MerchantServiceImpl implements MerchantService {

  @Autowired
  private MerchantRepository merchantRepository;

  @Autowired
  private UserRepository userRepository;

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
    User user = this.checkUser();

    Merchant checkMerchant = this.checkMerchant(user);

    Merchant updateMerchant = findByIdOrThrowNotFound(id);
    if (!updateMerchant.equals(checkMerchant)) {
      throw new BadRequestException("Please update for your own account");
    }
    updateMerchant.setName(merchant.getName());
    updateMerchant.setAddress(merchant.getAddress());
    return merchantRepository.save(updateMerchant);
  }

  @Override
  public String delete(String id) {
    User user = this.checkUser();

    Merchant checkMerchant = this.checkMerchant(user);

    Merchant merchant = findByIdOrThrowNotFound(id);
    if (!merchant.equals(checkMerchant)) {
      throw new BadRequestException("Please delete for your own account");
    }
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

  private User checkUser() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    String username = "a";

    if (principal instanceof UserDetails) {
      username = ((UserDetails)principal).getUsername();
    } else {
      username = principal.toString();
    }

    Optional<User> byUsername = userRepository.findByUsername(username);
    if (byUsername.isPresent()) {
      return byUsername.get();
    } else {
      throw new NotFoundException("User not found");
    }
  }

  private Merchant checkMerchant(User user) {
    Optional<Merchant> merchantByUserId = merchantRepository.getMerchantByUserId(user);
    if (merchantByUserId.isPresent()) {
      return merchantByUserId.get();
    } else {
      throw new NotFoundException("Merchant not found");
    }
  }
  
  
}
