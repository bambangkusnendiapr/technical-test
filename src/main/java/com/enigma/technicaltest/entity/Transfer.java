package com.enigma.technicaltest.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "trx_transfer")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Transfer {

  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  private String id;

  @ManyToOne(targetEntity = Customer.class, fetch = FetchType.EAGER)
  @JoinColumn(name = "from_customer")
  private Customer fromCustomer;

  @ManyToOne(targetEntity = Customer.class, fetch = FetchType.EAGER)
  @JoinColumn(name = "to_customer")
  private Customer toCustomer;

  private String debitOrCredit;

  private String description;

  private Integer nominal;

  @CreatedDate
  @Column(updatable = false)
  private Date createdAt;

  @PrePersist
  private void insertBefore() {
    if (this.createdAt == null) {
      this.createdAt = new Date();
    }
  }

  public Transfer(Customer fromCustomer, Customer toCustomer, String debitOrCredit, Integer nominal) {
    this.fromCustomer = fromCustomer;
    this.toCustomer = toCustomer;
    this.debitOrCredit = debitOrCredit;
    this.nominal = nominal;
  }

  public Transfer(Customer fromCustomer, Customer toCustomer, String debitOrCredit, String description, Integer nominal) {
    this.fromCustomer = fromCustomer;
    this.toCustomer = toCustomer;
    this.debitOrCredit = debitOrCredit;
    this.description = description;
    this.nominal = nominal;
  }
}
