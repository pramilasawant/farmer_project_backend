package com.shadervertex.farmerproduct.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shadervertex.farmerproduct.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
}