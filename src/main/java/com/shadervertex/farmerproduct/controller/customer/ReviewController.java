package com.shadervertex.farmerproduct.controller.customer;

import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shadervertex.farmerproduct.dto.OrderedProductsResponseDto;
import com.shadervertex.farmerproduct.dto.ReviewDto;
import com.shadervertex.farmerproduct.exceptions.ValidationException;
import com.shadervertex.farmerproduct.services.customer.review.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/ordered-products/{orderId}")
    public ResponseEntity<?> getOrderedProductDetailsByOrderId(@PathVariable Long orderId) {
        try {
            OrderedProductsResponseDto orderedProducts = reviewService.getOrderedProductsDetailsByOrderId(orderId);
            return ResponseEntity.ok(orderedProducts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error retrieving ordered products details: " + e.getMessage());
        }
    }

    @PostMapping("/review")
    public ResponseEntity<?> giveReview(@ModelAttribute ReviewDto reviewDto) {
        try {
            ReviewDto reviewDto2 = reviewService.giveReview(reviewDto);
            if (reviewDto2 == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Review could not be created");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(reviewDto2);
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation Error: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing review data: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error giving review: " + e.getMessage());
        }
    }
}
