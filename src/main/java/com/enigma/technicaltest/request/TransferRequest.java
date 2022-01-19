package com.enigma.technicaltest.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransferRequest {

  private String toAccountNumber;

  private Integer nominal;

  private String description;
}
