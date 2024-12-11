package com.shadervertex.farmerproduct.controller.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.shadervertex.farmerproduct.dto.FAQDto;
import com.shadervertex.farmerproduct.dto.ProductDto;
import com.shadervertex.farmerproduct.services.admin.adminproduct.AdminProductService;
import com.shadervertex.farmerproduct.services.admin.faq.FAQService;

import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService adminProductService;
    private final FAQService faqService;

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@ModelAttribute ProductDto productDto) {
        try {
            ProductDto productDto1 = adminProductService.addProduct(productDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(productDto1);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while adding the product: " + e.getMessage());
        }
    }

    @GetMapping("/products")
    public ResponseEntity<?> getAllProduct() {
        try {
            List<ProductDto> productDtos = adminProductService.getAllProducts();
            return ResponseEntity.ok(productDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching products: " + e.getMessage());
        }
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<?> getAllProductByName(@PathVariable String name) {
        try {
            List<ProductDto> productDtos = adminProductService.getAllProductsByName(name);
            return ResponseEntity.ok(productDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while searching products: " + e.getMessage());
        }
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        try {
            boolean deleted = adminProductService.deleteProduct(productId);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the product: " + e.getMessage());
        }
    }

    @PostMapping("/faq/{productId}")
    public ResponseEntity<?> postFAQ(@PathVariable Long productId, @RequestBody FAQDto faqDto) {
        try {
            FAQDto faqResponse = faqService.postFAQ(productId, faqDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(faqResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while posting the FAQ: " + e.getMessage());
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Long productId) {
        try {
            ProductDto productDto = adminProductService.getProductById(productId);
            if (productDto != null) {
                return ResponseEntity.ok(productDto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching the product: " + e.getMessage());
        }
    }

    @PutMapping("/product/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @ModelAttribute ProductDto productDto) {
        try {
            ProductDto updatedProduct = adminProductService.updateProduct(productId, productDto);
            if (updatedProduct != null) {
                return ResponseEntity.ok(updatedProduct);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An I/O error occurred while updating the product: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the product: " + e.getMessage());
        }
    }
}
