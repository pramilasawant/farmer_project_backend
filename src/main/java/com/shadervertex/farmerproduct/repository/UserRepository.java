package com.shadervertex.farmerproduct.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shadervertex.farmerproduct.enums.UserRole;
import com.shadervertex.farmerproduct.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findFirstByEmail(String email);

    // Used to check of ADMIN exists
    Optional<User> findByRole(UserRole userRole);
}