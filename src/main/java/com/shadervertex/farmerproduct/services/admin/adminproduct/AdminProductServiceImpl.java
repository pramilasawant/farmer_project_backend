package com.shadervertex.farmerproduct.services.admin.adminproduct;

import com.shadervertex.farmerproduct.dto.ProductDto;
import com.shadervertex.farmerproduct.model.CartItems;
import com.shadervertex.farmerproduct.model.Category;
import com.shadervertex.farmerproduct.model.Product;
import com.shadervertex.farmerproduct.repository.CartItemsRepository;
import com.shadervertex.farmerproduct.repository.CategoryRepository;
import com.shadervertex.farmerproduct.repository.ProductRepository;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CartItemsRepository cartItemsRepository; // Add CartItemsRepository

    @Override
    public ProductDto addProduct(ProductDto productDto) throws Exception {
        Category category = categoryRepository.findById(productDto.getCategoryId()).orElseThrow();

        Product product = Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .img(productDto.getImg().getBytes())
                .category(category)
                .build();

        Product savedProduct = productRepository.save(product);

        return savedProduct.getDto();
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(Product::getDto).collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getAllProductsByName(String name) {
        List<Product> products = productRepository.findAllByNameContaining(name);
        return products.stream().map(Product::getDto).collect(Collectors.toList());
    }

    @Override
    public boolean deleteProduct(Long id) {
        try {
            // Remove associated cart items first
            List<CartItems> cartItems = cartItemsRepository.findByProductId(id);
            cartItemsRepository.deleteAll(cartItems);

            // Delete the product
            Optional<Product> productOptional = productRepository.findById(id);
            if (productOptional.isPresent()) {
                productRepository.deleteById(id);
                return true;
            }
        } catch (DataIntegrityViolationException e) {
            // Handle the exception as needed, e.g., log the error or rethrow
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ProductDto getProductById(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        return optionalProduct.map(Product::getDto).orElse(null);
    }

    @Override
    public ProductDto updateProduct(Long productId, ProductDto productDto) throws IOException {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        Optional<Category> optionalCategory = categoryRepository.findById(productDto.getCategoryId());

        if (optionalProduct.isPresent() && optionalCategory.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(productDto.getName());
            product.setPrice(productDto.getPrice());
            product.setDescription(productDto.getDescription());
            product.setCategory(optionalCategory.get());

            // Only update the image if a new one is provided
            if (productDto.getImg() != null && !productDto.getImg().isEmpty()) {
                try {
                    product.setImg(productDto.getImg().getBytes());
                } catch (java.io.IOException e) {
                    // Handle the exception, e.g., log it or rethrow it
                    e.printStackTrace();
                    throw new IOException("Failed to get bytes from MultipartFile");
                }
            }
            Product updatedProduct = productRepository.save(product);
            return updatedProduct.getDto();
        }
        return null;
    }
}