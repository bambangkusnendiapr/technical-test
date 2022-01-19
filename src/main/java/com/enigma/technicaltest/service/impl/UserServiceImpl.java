package com.enigma.technicaltest.service.impl;

import com.enigma.technicaltest.entity.*;
import com.enigma.technicaltest.exception.NotFoundException;
import com.enigma.technicaltest.repository.AccountRepository;
import com.enigma.technicaltest.repository.CustomerRepository;
import com.enigma.technicaltest.repository.MerchantRepository;
import com.enigma.technicaltest.repository.UserRepository;
import com.enigma.technicaltest.response.RegisterCustomerResponse;
import com.enigma.technicaltest.response.RegisterMerchantResponse;
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

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private AccountRepository accountRepository;


    @Override
    public RegisterCustomerResponse createCustomer (User user, Customer customer, Set<Role> roles, String merchantId) {

        user.setRoles(roles);
        User saveUser = userRepository.save(user);

        String accountNumber = accountNumber();

        customer.setUserId(saveUser);
        customer.setAccountNumber(accountNumber);
        customer.setBalance(0);
        Customer saveCustomer = customerRepository.save(customer);

        Set<String> strRoles = new HashSet<>();
        for (Role role:saveUser.getRoles()) {
            strRoles.add(role.getRole().name());
        }

        Merchant merchant = findMerchant(merchantId);


        Account account = new Account();
        account.setMerchant(merchant);
        account.setCustomer(saveCustomer);
        account.setAccountNumber(accountNumber);
        account.setBalance(0);
        accountRepository.save(account);

        return new RegisterCustomerResponse(
                saveUser.getId(),
                saveUser.getUsername(),
                saveUser.getEmail(),
                saveCustomer.getId(),
                saveCustomer.getName(),
                merchant.getName(),
                account.getAccountNumber(),
                saveUser.getCreatedAt(),
                saveUser.getUpdatedAt(),
                strRoles
        );
    }

    @Override
    public RegisterMerchantResponse createMerchant(User user, Merchant merchant, Set<Role> roles) {
        user.setRoles(roles);
        User saveUser = userRepository.save(user);

        merchant.setUserId(saveUser);
        Merchant saveMerchant = merchantRepository.save(merchant);

        Set<String> strRoles = new HashSet<>();
        for (Role role:saveUser.getRoles()) {
            strRoles.add(role.getRole().name());
        }
        return new RegisterMerchantResponse(
                saveUser.getId(),
                saveUser.getUsername(),
                saveUser.getEmail(),
                saveMerchant.getId(),
                saveMerchant.getName(),
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

    private Merchant findMerchant(String merchantId) {
        Optional<Merchant> byId = merchantRepository.findById(merchantId);
        if (byId.isPresent()) {
            return byId.get();
        } else {
            throw new NotFoundException("Merchant not found");
        }
    }


}
