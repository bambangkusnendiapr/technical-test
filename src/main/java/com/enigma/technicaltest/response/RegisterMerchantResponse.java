package com.enigma.technicaltest.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterMerchantResponse {

  private String userId;

  private String username;

  private String email;

  private String merchantId;

  private String merchantName;

  private Date createdAt;

  private Date updatedAt;

  private Set<String> roles;

}
