package com.shadervertex.farmerproduct.services.customer.review;

import java.io.IOException;

import com.shadervertex.farmerproduct.dto.OrderedProductsResponseDto;
import com.shadervertex.farmerproduct.dto.ReviewDto;

public interface ReviewService {
	OrderedProductsResponseDto getOrderedProductsDetailsByOrderId(Long orderId);
	
	ReviewDto giveReview(ReviewDto reviewDto) throws IOException ;
}
