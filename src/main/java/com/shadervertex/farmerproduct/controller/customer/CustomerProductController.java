package com.shadervertex.farmerproduct.controller.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shadervertex.farmerproduct.dto.ProductDetailDto;
import com.shadervertex.farmerproduct.dto.ProductDto;
import com.shadervertex.farmerproduct.exceptions.ValidationException;
import com.shadervertex.farmerproduct.services.customer.CustomerProductService;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerProductController {

    private final CustomerProductService customerProductService;

    @GetMapping("/products")
    public ResponseEntity<?> getAllProduct() {
        try {
            List<ProductDto> productDtos = customerProductService.getAllProducts();
            return ResponseEntity.ok(productDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving products: " + e.getMessage());
        }
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<?> getAllProductByName(@PathVariable String name) {
        try {
            List<ProductDto> productDtos = customerProductService.getAllProductsByName(name);
            return ResponseEntity.ok(productDtos);
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error searching products by name: " + e.getMessage());
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getProductDetailById(@PathVariable Long productId) {
        try {
            ProductDetailDto productDetailDto = customerProductService.getProductDetailById(productId);
            if (productDetailDto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(productDetailDto);
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving product details: " + e.getMessage());
        }
    }
}
