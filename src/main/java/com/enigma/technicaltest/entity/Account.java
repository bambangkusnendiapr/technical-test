package com.enigma.technicaltest.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "mst_account")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Account {

  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  private String id;

  @ManyToOne(targetEntity = Merchant.class, fetch = FetchType.EAGER)
  @JoinColumn(name = "merchant_id")
  private Merchant merchant;

  @ManyToOne(targetEntity = Customer.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id")
  @JsonBackReference
  private Customer customer;

  private String accountNumber;

  private Integer balance;

  @CreatedDate
  @Column(updatable = false)
  private Date createdAt;

  @PrePersist
  private void insertBefore() {
    if (this.createdAt == null) {
      this.createdAt = new Date();
    }
  }

}
