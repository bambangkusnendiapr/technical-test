package com.enigma.technicaltest.service;

import com.enigma.technicaltest.entity.*;
import com.enigma.technicaltest.response.RegisterCustomerResponse;
import com.enigma.technicaltest.response.RegisterMerchantResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Set;

public interface UserService extends UserDetailsService {

    RegisterCustomerResponse createCustomer (User user, Customer customer, Set<Role> roles);

    RegisterMerchantResponse createMerchant (User user, Merchant merchant, Set<Role> roles);
}
