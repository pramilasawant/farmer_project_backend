package com.shadervertex.farmerproduct.services.customer.wishlist;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.shadervertex.farmerproduct.dto.WishlistDto;
import com.shadervertex.farmerproduct.model.User;
import com.shadervertex.farmerproduct.model.Wishlist;
import com.shadervertex.farmerproduct.repository.ProductRepository;
import com.shadervertex.farmerproduct.repository.UserRepository;
import com.shadervertex.farmerproduct.repository.WishlistRepository;
import com.shadervertex.farmerproduct.model.Product;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService{
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final WishlistRepository wishlistRepository;
	
	public WishlistDto addProductToWishlist( WishlistDto wishlistDto) {
		Optional<Product> optionalProduct = productRepository.findById(wishlistDto.getProductId());
		Optional<User> optionalUser = userRepository.findById(wishlistDto.getUserId());
		
		if(optionalProduct.isPresent()&& optionalUser.isPresent()) {
			Wishlist wishlist = new Wishlist();
			wishlist.setProduct(optionalProduct.get());
			wishlist.setUser(optionalUser.get());
			
			return wishlistRepository.save(wishlist).getWishlistDto();
			
		}
		return null;
	}
	
	public  List<WishlistDto> getWishlistByUserId(Long userId){
		return wishlistRepository.findAllByUserId(userId).stream().map(Wishlist::getWishlistDto).collect(Collectors.toList());
	}
}