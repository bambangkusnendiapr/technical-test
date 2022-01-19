package com.enigma.technicaltest.service;

import com.enigma.technicaltest.dto.CustomerDTO;
import com.enigma.technicaltest.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface CustomerService {
    Page<Customer> getAll (Pageable pageable, CustomerDTO customerDTO, Sort sort);
    Customer getById(String id);
    Customer update ( String id,Customer customer);
    String delete (String id);
}
