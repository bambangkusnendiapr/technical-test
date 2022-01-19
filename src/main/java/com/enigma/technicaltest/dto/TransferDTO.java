package com.enigma.technicaltest.dto;

import com.enigma.technicaltest.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransferDTO {
  private String fromDate;
  private String toDate;
  private Customer customerId;
}
