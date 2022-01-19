package com.enigma.technicaltest.service.impl;

import com.enigma.technicaltest.entity.Role;
import com.enigma.technicaltest.exception.NotFoundException;
import com.enigma.technicaltest.repository.RoleRepository;
import com.enigma.technicaltest.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.enigma.technicaltest.entity.UserRole.*;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repository;
    @Override
    public Role create(String strRole) {
        if(strRole == null){
            if(!repository.existsByRole(CUSTOMER_ROLE)){
                Role userRole = new Role(CUSTOMER_ROLE);
                return repository.save(userRole);
            }
            return repository.findByRole(CUSTOMER_ROLE).orElseThrow(() -> new NotFoundException("Error: Role Not Found"));

        } else {
            if (strRole.equalsIgnoreCase("customer")){
                if (!repository.existsByRole(CUSTOMER_ROLE)){

                    Role adminRole = new Role(CUSTOMER_ROLE);
                    return repository.save(adminRole);
                }
                return repository.findByRole(CUSTOMER_ROLE).orElseThrow(()-> new NotFoundException("Error: Role Not Found"));
            }

            if (strRole.equalsIgnoreCase("merchant")){
                if (!repository.existsByRole(MERCHANT_ROLE)){

                    Role adminRole = new Role(MERCHANT_ROLE);
                    return repository.save(adminRole);
                }
                return repository.findByRole(MERCHANT_ROLE).orElseThrow(()-> new NotFoundException("Error: Role Not Found"));
            }
        }

        return repository.findByRole(CUSTOMER_ROLE).orElseThrow(() -> new NotFoundException("Error: Role Not Found"));
    }
}
