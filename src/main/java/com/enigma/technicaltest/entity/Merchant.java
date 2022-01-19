package com.enigma.technicaltest.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "mst_merchant")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SQLDelete(sql = "UPDATE mst_merchant SET isDeleted = true WHERE id=?")
@Where(clause = "is_Deleted=false")
public class Merchant {

  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  private String id;

  @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private User userId;

  private String name;

  @Column(name = "customer_address")
  private String address;

  @CreatedDate
  @Column(updatable = false)
  private Date createdAt;

  @LastModifiedDate
  private Date updatedAt;

  private Boolean isDeleted;

  @PrePersist
  private void insertBefore() {
    if (this.createdAt == null) {
      this.createdAt = new Date();
    }

    if (this.updatedAt == null) {
      this.updatedAt = new Date();
    }

    if (isDeleted == null) isDeleted = false;
  }
  @PreUpdate
  private void updateBefore() {
    this.updatedAt = new Date();
  }

}
