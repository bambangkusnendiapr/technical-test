package com.enigma.technicaltest.service;

import com.enigma.technicaltest.entity.*;
import com.enigma.technicaltest.response.RegisterResponse;
import javassist.NotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Set;

public interface UserService extends UserDetailsService {
    RegisterResponse createCustomer (User user, Customer customer, Set<Role> roles);
}
