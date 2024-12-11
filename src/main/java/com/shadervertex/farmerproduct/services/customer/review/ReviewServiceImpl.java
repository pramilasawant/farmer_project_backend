package com.shadervertex.farmerproduct.services.customer.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.shadervertex.farmerproduct.dto.OrderedProductsResponseDto;
import com.shadervertex.farmerproduct.dto.ProductDto;
import com.shadervertex.farmerproduct.dto.ReviewDto;
import com.shadervertex.farmerproduct.model.CartItems;
import com.shadervertex.farmerproduct.model.Order;
import com.shadervertex.farmerproduct.model.Product;
import com.shadervertex.farmerproduct.model.Review;
import com.shadervertex.farmerproduct.model.User;
import com.shadervertex.farmerproduct.repository.OrderRepository;
import com.shadervertex.farmerproduct.repository.ProductRepository;
import com.shadervertex.farmerproduct.repository.ReviewRepository;
import com.shadervertex.farmerproduct.repository.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final ReviewRepository reviewRepository;

    public OrderedProductsResponseDto getOrderedProductsDetailsByOrderId(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        OrderedProductsResponseDto orderedProductsResponseDto = new OrderedProductsResponseDto();
        if (optionalOrder.isPresent()) {
            orderedProductsResponseDto.setOrderAmount(optionalOrder.get().getAmount());

            List<ProductDto> productDtoList = new ArrayList<>();
            for (CartItems cartItems : optionalOrder.get().getCartItems()) {
                ProductDto productDto = ProductDto.builder()
                        .id(cartItems.getProduct().getId())
                        .name(cartItems.getProduct().getName())
                        .price(cartItems.getPrice())
                        .quantity(cartItems.getQuantity())
                        .byteImg(cartItems.getProduct().getImg())
                        .build();

                productDtoList.add(productDto);
            }

            orderedProductsResponseDto.setProductDtoList(productDtoList);
        }
        return orderedProductsResponseDto;
    }

    public ReviewDto giveReview(ReviewDto reviewDto) throws IOException {
        Optional<Product> optionalProduct = productRepository.findById(reviewDto.getProductId());
        Optional<User> optionalUser = userRepository.findById(reviewDto.getUserId());

        if (optionalProduct.isPresent() && optionalUser.isPresent()) {
            Review review = new Review();

            review.setDescription(reviewDto.getDescription());
            review.setRating(reviewDto.getRating());
            review.setUser(optionalUser.get());
            review.setProduct(optionalProduct.get());
            review.setImg(reviewDto.getImg().getBytes());

            return reviewRepository.save(review).getDto();
        }
        return null;
    }
}