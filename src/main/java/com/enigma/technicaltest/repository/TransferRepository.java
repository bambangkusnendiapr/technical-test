package com.enigma.technicaltest.repository;

import com.enigma.technicaltest.entity.Customer;
import com.enigma.technicaltest.entity.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface TransferRepository extends JpaRepository<Transfer, String> {

  Page<Transfer> findAll(Specification<Transfer> specification, Pageable pageable);
}
