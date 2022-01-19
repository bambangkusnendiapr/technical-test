package com.enigma.technicaltest.controller;

import com.enigma.technicaltest.entity.*;
import com.enigma.technicaltest.request.LoginRequest;
import com.enigma.technicaltest.request.RegisterCustomerRequest;
import com.enigma.technicaltest.request.RegisterMerchantRequest;
import com.enigma.technicaltest.response.LoginResponse;
import com.enigma.technicaltest.response.RegisterCustomerResponse;
import com.enigma.technicaltest.response.RegisterMerchantResponse;
import com.enigma.technicaltest.response.WebResponse;
import com.enigma.technicaltest.security.jwt.JwtUtils;
import com.enigma.technicaltest.service.CustomerService;
import com.enigma.technicaltest.service.RoleService;
import com.enigma.technicaltest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register/customer")
    public ResponseEntity<WebResponse<?>> registerCustomer (@Valid @RequestBody RegisterCustomerRequest request){

            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            Customer customer = new Customer();
            customer.setName(request.getName());

            Set<String> roles = new HashSet<>();
            roles.add("customer");
            Set<Role> roleSet = new HashSet<>();
            for (String role: roles) {
                Role role1 = roleService.create(role);
                roleSet.add(role1);
            }

            RegisterCustomerResponse customerCreate = userService.createCustomer(user,customer,roleSet);
            WebResponse<?> response = new WebResponse<>("Successfully Create New Customer",customerCreate);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(response);
    }

  @PostMapping("/register/merchant")
  public ResponseEntity<WebResponse<?>> registerMerchant(@Valid @RequestBody RegisterMerchantRequest request){

    User user = new User();
    user.setUsername(request.getUsername());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    Merchant merchant = new Merchant();
    merchant.setName(request.getName());

    Set<String> roles = new HashSet<>();
    roles.add("merchant");
    Set<Role> roleSet = new HashSet<>();
    for (String role: roles) {
      Role role1 = roleService.create(role);
      roleSet.add(role1);
    }

    RegisterMerchantResponse merchantCreate = userService.createMerchant(user,merchant,roleSet);
    WebResponse<?> response = new WebResponse<>("Successfully Create New Merchant",merchantCreate);

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
  }

    @PostMapping("/login")
    public ResponseEntity<WebResponse<?>> login (@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

      SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
        Set<String> roles = new HashSet<>();
        for (GrantedAuthority authority : userDetail.getAuthorities()) {
            roles.add(authority.getAuthority());
        }

        LoginResponse loginResponse = new LoginResponse(jwt,roles);
        WebResponse<?> response = new WebResponse<>("Login Success",loginResponse);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

}
