package com.ecommerce.api.security.services;

import java.util.Optional;

import javax.transaction.Transactional;

import com.ecommerce.api.security.entities.Role;
import com.ecommerce.api.security.enums.RoleList;
import com.ecommerce.api.security.respositories.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    public Optional<Role> getByRoleName(RoleList roleName){
        return roleRepository.findByRoleName(roleName);
    }


}
