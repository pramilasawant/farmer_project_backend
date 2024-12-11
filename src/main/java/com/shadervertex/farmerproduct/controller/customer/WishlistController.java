package com.shadervertex.farmerproduct.controller.customer;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shadervertex.farmerproduct.dto.WishlistDto;
import com.shadervertex.farmerproduct.exceptions.ValidationException;
import com.shadervertex.farmerproduct.services.customer.wishlist.WishlistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/wishlist")
    public ResponseEntity<?> addProductToWishlist(@RequestBody WishlistDto wishlistDto) {
        try {
            WishlistDto postedWishlistDto = wishlistService.addProductToWishlist(wishlistDto);
            if (postedWishlistDto == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong while adding the product to wishlist");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(postedWishlistDto);
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error adding product to wishlist: " + e.getMessage());
        }
    }

    @GetMapping("/wishlist/{userId}")
    public ResponseEntity<?> getWishlistByUserId(@PathVariable Long userId) {
        try {
            List<WishlistDto> wishlist = wishlistService.getWishlistByUserId(userId);
            return ResponseEntity.ok(wishlist);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error retrieving wishlist: " + e.getMessage());
        }
    }
}
