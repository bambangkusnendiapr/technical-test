package com.enigma.technicaltest.service.impl;

import com.enigma.technicaltest.dto.TransferDTO;
import com.enigma.technicaltest.entity.Customer;
import com.enigma.technicaltest.entity.Transfer;
import com.enigma.technicaltest.entity.User;
import com.enigma.technicaltest.exception.BadRequestException;
import com.enigma.technicaltest.exception.NotFoundException;
import com.enigma.technicaltest.repository.CustomerRepository;
import com.enigma.technicaltest.repository.TransferRepository;
import com.enigma.technicaltest.repository.UserRepository;
import com.enigma.technicaltest.request.FillInBalanceRequest;
import com.enigma.technicaltest.request.TransferRequest;
import com.enigma.technicaltest.service.TransferService;
import com.enigma.technicaltest.specification.CustomerSpecification;
import com.enigma.technicaltest.specification.TransferSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransferServiceImpl implements TransferService {

  @Autowired
  private TransferRepository transferRepository;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private UserRepository userRepository;

  @Override
  public Page<Transfer> getAll(Pageable pageable, TransferDTO transferDTO) {
    User user = checkUser();
    Customer customer = checkCustomer(user);
    transferDTO.setCustomerId(customer);
    Specification<Transfer> specification = TransferSpecification.getSpecification(transferDTO);
    return transferRepository.findAll(specification, pageable);
  }

  @Override
  public Transfer fillInBalance(FillInBalanceRequest request) {

    User user = checkUser();

    Customer customer = checkCustomer(user);
    customer.setBalance(customer.getBalance() + request.getNominal());
    customerRepository.save(customer);

    Transfer transfer = new Transfer(customer, customer, "debit", request.getNominal());
    transferRepository.save(transfer);

    return transfer;
  }

  @Override
  public Transfer transfer(TransferRequest request) {
    if (request.getNominal() < 1) {
      throw new BadRequestException("Nominal can not be zero or minus");
    }

    User user = checkUser();
    Customer fromCustomer = checkCustomer(user);
    if (request.getToAccountNumber().equalsIgnoreCase(fromCustomer.getAccountNumber())) {
      throw new BadRequestException("It is forbidden to transfer to your own account number. Please fill in balance");
    }

    if (fromCustomer.getBalance() < request.getNominal()) {
      throw new BadRequestException("Your balance is not enough");
    }

    fromCustomer.setBalance(fromCustomer.getBalance() - request.getNominal());
    customerRepository.save(fromCustomer);

    Customer toCustomer = getCustomer(request.getToAccountNumber());
    toCustomer.setBalance(toCustomer.getBalance() + request.getNominal());
    customerRepository.save(toCustomer);

    Transfer credit = new Transfer(fromCustomer, toCustomer, "credit", request.getDescription(), request.getNominal());
    transferRepository.save(credit);

    Transfer debit = new Transfer(toCustomer, fromCustomer, "debit", request.getDescription(), request.getNominal());
    transferRepository.save(debit);

    return credit;
  }

  private Customer getCustomer(String accountNumber) {
    Optional<Customer> customerByAccountNumber = customerRepository.getCustomerByAccountNumber(accountNumber);
    if (customerByAccountNumber.isPresent()) {
      return customerByAccountNumber.get();
    } else {
      throw new NotFoundException("Account number not found");
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

  private Customer checkCustomer(User user) {
    Optional<Customer> customerByUserId = customerRepository.getCustomerByUserId(user);
    if (customerByUserId.isPresent()) {
      return customerByUserId.get();
    } else {
      throw new NotFoundException("Customer not found");
    }
  }
}
