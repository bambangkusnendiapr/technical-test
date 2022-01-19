package com.enigma.technicaltest.repository;


import com.enigma.technicaltest.entity.Role;
import com.enigma.technicaltest.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findByRole (UserRole role);
    Boolean existsByRole(UserRole role);
}
