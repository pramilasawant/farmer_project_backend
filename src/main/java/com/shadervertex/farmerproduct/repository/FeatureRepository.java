
package com.shadervertex.farmerproduct.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shadervertex.farmerproduct.model.Feature;

public interface FeatureRepository extends JpaRepository<Feature, Long> {
    Optional<Feature> findById(Long featureId); 
}