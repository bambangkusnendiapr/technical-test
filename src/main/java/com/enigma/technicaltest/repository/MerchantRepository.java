package com.enigma.technicaltest.repository;

import com.enigma.technicaltest.entity.Customer;
import com.enigma.technicaltest.entity.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, String> {
  Page<Merchant> findAll(Specification<Merchant> specification, Pageable pageable);
}
