package com.enigma.technicaltest.service.impl;

import com.enigma.technicaltest.dto.CustomerDTO;
import com.enigma.technicaltest.entity.Customer;
import com.enigma.technicaltest.exception.NotFoundException;
import com.enigma.technicaltest.repository.CustomerRepository;
import com.enigma.technicaltest.service.CustomerService;
import com.enigma.technicaltest.specification.CustomerSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    //Get All data customer
    @Override
    public Page<Customer> getAll(Pageable pageable, CustomerDTO customerDTO, Sort sort) {
        Specification<Customer> specification = CustomerSpecification.getSpecification(customerDTO);
        return customerRepository.findAll(specification,pageable);
    }

    @Override
    public Customer getById(String id) {
        return findByIdOrThrowNotFound(id);
    }


    @Override
    public Customer update(String id, Customer customer) {
        Customer updateCustomer = findByIdOrThrowNotFound(id);
        updateCustomer.setName(customer.getName());
        updateCustomer.setAddress(customer.getAddress());
        updateCustomer.setPhone(customer.getPhone());

        return customerRepository.save(updateCustomer);
    }

    @Override
    public String delete(String id) {
        Customer customer = findByIdOrThrowNotFound(id);
        if (customer.getIsDeleted()) {
            throw new NotFoundException("Customer not found");
        }else {
            customer.setIsDeleted(true);
            customerRepository.save(customer);
        }
        return "Customer has been deleted";
    }
    private Customer findByIdOrThrowNotFound(String id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            return customer.get();
        } else {
            throw new NotFoundException("Customer not found");
        }
    }


}
