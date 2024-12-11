package com.shadervertex.farmerproduct.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shadervertex.farmerproduct.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findById(Long roleId); 
}