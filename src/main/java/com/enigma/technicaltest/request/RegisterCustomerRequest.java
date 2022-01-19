package com.enigma.technicaltest.request;

import com.enigma.technicaltest.entity.Merchant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterCustomerRequest {

  private String username;

  private String email;

  private String password;

  private String name;

  private String merchantId;

}
