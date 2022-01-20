package com.enigma.technicaltest.repository;

import com.enigma.technicaltest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
        Optional<User> findByUsername(String username);

        @Query(value = "SELECT c FROM User c WHERE c.email = ?1")
        Optional<User> findByEmail(String email);
}
