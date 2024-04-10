package com.ecommerce.api.security.respositories;

import java.util.Optional;

import com.ecommerce.api.security.entities.Role;
import com.ecommerce.api.security.enums.RoleList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(RoleList roleName);
}