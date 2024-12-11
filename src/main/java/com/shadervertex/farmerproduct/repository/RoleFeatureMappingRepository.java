package com.shadervertex.farmerproduct.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shadervertex.farmerproduct.model.Feature;
import com.shadervertex.farmerproduct.model.Role;
import com.shadervertex.farmerproduct.model.RoleFeatureMapping;

public interface RoleFeatureMappingRepository extends JpaRepository<RoleFeatureMapping, Long> {
    
    // Method to check if a mapping between a specific role and feature already exists
    boolean existsByRoleAndFeature(Role role, Feature feature);
}