package com.enigma.technicaltest.repository;

import com.enigma.technicaltest.entity.Customer;
import com.enigma.technicaltest.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    Page<Customer> findAll(Pageable pageable);

    Page<Customer> findAll(Specification<Customer> specification, Pageable pageable);

    @Query(value = "SELECT c FROM Customer c WHERE c.accountNumber = ?1")
    Optional<Customer> getCustomerByAccountNumber(String accountNumber);

    @Override
    List<Customer> findAll();

    @Query(value = "SELECT c FROM Customer c WHERE c.userId = ?1")
    Optional<Customer> getCustomerByUserId(User user);
}
