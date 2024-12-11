package com.shadervertex.farmerproduct.services.customer;

import java.util.List;

import com.shadervertex.farmerproduct.dto.ProductDetailDto;
import com.shadervertex.farmerproduct.dto.ProductDto;

public interface CustomerProductService {
	List<ProductDto> getAllProducts();
	List<ProductDto> getAllProductsByName(String name);
	
	ProductDetailDto getProductDetailById(Long productId);
}