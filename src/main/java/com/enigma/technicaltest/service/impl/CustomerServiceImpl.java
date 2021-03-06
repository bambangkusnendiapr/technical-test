package com.enigma.technicaltest.service.impl;

import com.enigma.technicaltest.dto.CustomerDTO;
import com.enigma.technicaltest.entity.Customer;
import com.enigma.technicaltest.entity.Merchant;
import com.enigma.technicaltest.entity.User;
import com.enigma.technicaltest.exception.BadRequestException;
import com.enigma.technicaltest.exception.NotFoundException;
import com.enigma.technicaltest.repository.CustomerRepository;
import com.enigma.technicaltest.repository.MerchantRepository;
import com.enigma.technicaltest.repository.UserRepository;
import com.enigma.technicaltest.service.CustomerService;
import com.enigma.technicaltest.specification.CustomerSpecification;
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
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MerchantRepository merchantRepository;

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
        User user = this.checkUser();

        Customer checkCustomer = this.checkCustomer(user);
        Merchant merchant = null;
        if (checkCustomer == null) {
            merchant = this.checkMerchant(user);
        } else {
            if (!checkCustomer.getId().equalsIgnoreCase(id)) {
                throw new BadRequestException("Please update for your own account");
            }
        }

        Customer updateCustomer = findByIdOrThrowNotFound(id);
        if (!updateCustomer.getMerchant().equals(merchant) && merchant != null) {
            throw new BadRequestException("Please update for your customer");
        }
        updateCustomer.setName(customer.getName());
        updateCustomer.setAddress(customer.getAddress());
        updateCustomer.setPhone(customer.getPhone());

        return customerRepository.save(updateCustomer);
    }

    @Override
    public String delete(String id) {
        User user = this.checkUser();

        Customer checkCustomer = this.checkCustomer(user);
        Merchant merchant = null;
        if (checkCustomer == null) {
            merchant = this.checkMerchant(user);
        } else {
            if (!checkCustomer.getId().equalsIgnoreCase(id)) {
                throw new BadRequestException("Please delete for your own account");
            }
        }

        Customer customer = findByIdOrThrowNotFound(id);
        if (!customer.getMerchant().equals(merchant) && merchant != null) {
            throw new BadRequestException("Please delete for your customer");
        }
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
            return null;
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
