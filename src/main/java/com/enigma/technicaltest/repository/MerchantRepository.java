package com.enigma.technicaltest.repository;

import com.enigma.technicaltest.entity.Customer;
import com.enigma.technicaltest.entity.Merchant;
import com.enigma.technicaltest.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, String> {
  Page<Merchant> findAll(Specification<Merchant> specification, Pageable pageable);

  @Query(value = "SELECT c FROM Merchant c WHERE c.userId = ?1")
  Optional<Merchant> getMerchantByUserId(User user);
}
