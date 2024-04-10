package com.ecommerce.api.security.services;

import java.util.Optional;

import javax.annotation.PostConstruct;
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


    public void createDefaultRoles() {
        // Verificar si los roles ya existen en la base de datos
        Optional<Role> roleUser = roleRepository.findByRoleName(RoleList.ROLE_USER);
        Optional<Role> roleAdmin = roleRepository.findByRoleName(RoleList.ROLE_ADMIN);

        if (roleUser.isEmpty()) {
            roleRepository.save(new Role(RoleList.ROLE_USER));
        }

        if (roleAdmin.isEmpty()) {
            roleRepository.save(new Role(RoleList.ROLE_ADMIN));
        }
    }

    @PostConstruct
    public void init() {
        createDefaultRoles();
    }
 }
