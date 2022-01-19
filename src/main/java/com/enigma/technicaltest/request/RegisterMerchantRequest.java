package com.enigma.technicaltest.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterMerchantRequest {
  private String username;

  private String email;

  private String password;

  private String name;
}
