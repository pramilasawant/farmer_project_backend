package com.shadervertex.farmerproduct.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shadervertex.farmerproduct.model.FarmerProductRequest;

@Repository
public interface FarmerProductRequestRepository extends JpaRepository<FarmerProductRequest, Long> {
    List<FarmerProductRequest> findAllByIsApproved(Boolean isApproved);
}