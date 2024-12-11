package com.shadervertex.farmerproduct.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.shadervertex.farmerproduct.dto.OrderDto;
import com.shadervertex.farmerproduct.services.customer.cart.CartService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TrackingController {
    private final CartService cartService;

    @GetMapping("/order/{trackingId}")
    public ResponseEntity<?> searchOrderByTrackingId(@PathVariable UUID trackingId) {
        try {
            OrderDto orderDto = cartService.searchOrderByTrackingId(trackingId);
            if (orderDto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
            }
            return ResponseEntity.ok(orderDto);
        } catch (Exception e) {
            log.error("Error retrieving order by tracking ID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
}
