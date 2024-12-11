package com.shadervertex.farmerproduct.services.customer.wishlist;

import java.util.List;

import com.shadervertex.farmerproduct.dto.WishlistDto;

public interface WishlistService {
	WishlistDto addProductToWishlist( WishlistDto wishlistDto);
	
	List<WishlistDto> getWishlistByUserId(Long userId);
}