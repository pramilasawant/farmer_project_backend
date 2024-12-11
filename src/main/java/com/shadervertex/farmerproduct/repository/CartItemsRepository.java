package com.shadervertex.farmerproduct.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shadervertex.farmerproduct.model.CartItems;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Long> {

    // Find cart items by product ID
    List<CartItems> findByProductId(Long productId);

    // Find a cart item by product ID, order ID, and user ID
    Optional<CartItems> findByProductIdAndOrderIdAndUserId(Long productId, Long orderId, Long userId);
}