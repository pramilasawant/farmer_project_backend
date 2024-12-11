package com.shadervertex.farmerproduct.services.admin.adminproduct;

import java.util.List;

import com.shadervertex.farmerproduct.dto.ProductDto;

import io.jsonwebtoken.io.IOException;

public interface AdminProductService {
    ProductDto addProduct(ProductDto productDto) throws Exception;
    List<ProductDto> getAllProducts();
    List<ProductDto> getAllProductsByName(String name);
    boolean deleteProduct(Long id);
    ProductDto getProductById(Long productId);
    ProductDto updateProduct(Long productId, ProductDto productDto) throws IOException;
}