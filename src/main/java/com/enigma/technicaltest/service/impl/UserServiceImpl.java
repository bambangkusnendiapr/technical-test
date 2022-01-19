package com.enigma.technicaltest.service.impl;

import com.enigma.technicaltest.entity.*;
import com.enigma.technicaltest.exception.NotFoundException;
import com.enigma.technicaltest.repository.CustomerRepository;
import com.enigma.technicaltest.repository.UserRepository;
import com.enigma.technicaltest.response.RegisterResponse;
import com.enigma.technicaltest.service.RoleService;
import com.enigma.technicaltest.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;


    @Override
    public RegisterResponse createCustomer (User user, Customer customer, Set<Role> roles) {

        user.setRoles(roles);
        User saveUser = userRepository.save(user);

        String accountNumber = accountNumber();

        customer.setAccountNumber(accountNumber);
        customer.setBalance(0);
        customer.setUserId(saveUser);
        Customer saveCustomer = customerRepository.save(customer);

        Set<String> strRoles = new HashSet<>();
        for (Role role:saveUser.getRoles()) {
            strRoles.add(role.getRole().name());
        }
        return new RegisterResponse(
                saveUser.getId(),
                saveUser.getUsername(),
                saveUser.getEmail(),
                saveCustomer.getId(),
                saveCustomer.getName(),
                saveCustomer.getAccountNumber(),
                saveUser.getCreatedAt(),
                saveUser.getUpdatedAt(),
                strRoles
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Username Not Registered"));
        return UserDetailImpl.build(user);
    }

    private String accountNumber() {
        Random random = new Random();
        int result;
        String accountNumber = "1";
        for ( int counter = 1; counter <= 9; counter++ ){
            result = 1 + random.nextInt( 9 );
            accountNumber = accountNumber + result;
        }

        List<Customer> customers = customerRepository.findAll();
        for (Customer customer : customers) {
            if (customer.getAccountNumber().equalsIgnoreCase(accountNumber)) {
                return accountNumber();
            }
        }

        return accountNumber;
    }


}
