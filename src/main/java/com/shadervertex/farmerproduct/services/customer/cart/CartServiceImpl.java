package com.shadervertex.farmerproduct.services.customer.cart;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.shadervertex.farmerproduct.dto.AddProductInCartDto;
import com.shadervertex.farmerproduct.dto.CartItemsDto;
import com.shadervertex.farmerproduct.dto.OrderDto;
import com.shadervertex.farmerproduct.dto.PlaceOrderDto;
import com.shadervertex.farmerproduct.enums.OrderStatus;
import com.shadervertex.farmerproduct.exceptions.ValidationException;
import com.shadervertex.farmerproduct.model.CartItems;
import com.shadervertex.farmerproduct.model.Coupon;
import com.shadervertex.farmerproduct.model.Order;
import com.shadervertex.farmerproduct.model.Product;
import com.shadervertex.farmerproduct.model.User;
import com.shadervertex.farmerproduct.repository.CartItemsRepository;
import com.shadervertex.farmerproduct.repository.CouponRepository;
import com.shadervertex.farmerproduct.repository.OrderRepository;
import com.shadervertex.farmerproduct.repository.ProductRepository;
import com.shadervertex.farmerproduct.repository.UserRepository;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import org.json.JSONException;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {

    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final CartItemsRepository cartItemsRepository;

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final CouponRepository couponRepository;

    // Method to add a product to the cart
    public ResponseEntity<?> addProductToCart(AddProductInCartDto addProductInCartDto) {
        System.out.println(addProductInCartDto.toString());

        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.Pending);

        if (activeOrder == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order is empty");
        }

        // Check if the product is already present in the cart
        Optional<CartItems> optionalCartItems = cartItemsRepository.findByProductIdAndOrderIdAndUserId(
                addProductInCartDto.getProductId(), activeOrder.getId(), addProductInCartDto.getUserId());

        if (optionalCartItems.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } else {
            Optional<Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());
            Optional<User> optionalUser = userRepository.findById(addProductInCartDto.getUserId());

            if (optionalUser.isPresent() && optionalProduct.isPresent()) {
                CartItems cartItems = new CartItems();
                cartItems.setProduct(optionalProduct.get());
                cartItems.setPrice(optionalProduct.get().getPrice());
                cartItems.setQuantity(1L);
                cartItems.setUser(optionalUser.get());
                cartItems.setOrder(activeOrder);

                cartItemsRepository.save(cartItems); // Updated line

                activeOrder.setTotalAmount(activeOrder.getTotalAmount() + cartItems.getPrice());
                activeOrder.setAmount(activeOrder.getAmount() + cartItems.getPrice());
                activeOrder.getCartItems().add(cartItems);

                orderRepository.save(activeOrder);

                return ResponseEntity.status(HttpStatus.CREATED).body(cartItems);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or product not found");
            }
        }
    }

    // Method to get the cart details by user ID
    public OrderDto getCartByUserId(Long userId) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.Pending);

        if (activeOrder == null) {
            return null;
        }

        List<CartItemsDto> cartItemsDtosList = activeOrder.getCartItems().stream()
                .map(CartItems::getCartDto)
                .collect(Collectors.toList());

        OrderDto orderDto = new OrderDto();
        orderDto.setId(activeOrder.getId());
        orderDto.setAmount(activeOrder.getAmount());
        orderDto.setOrderStatus(activeOrder.getOrderStatus());
        orderDto.setDiscount(activeOrder.getDiscount());
        orderDto.setTotalAmount(activeOrder.getTotalAmount());
        orderDto.setCartItems(cartItemsDtosList);

        if (activeOrder.getCoupon() != null) {
            orderDto.setCouponCode(activeOrder.getCoupon().getCode());
            orderDto.setCouponName(activeOrder.getCoupon().getName());
        }

        return orderDto;
    }

    // Method to apply a coupon to the cart
    public OrderDto applyCoupon(Long userId, String code) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.Pending);
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new ValidationException("Coupon not found"));

        if (couponIsExpired(coupon)) {
            throw new ValidationException("Coupon is expired");
        }

        double discountAmount = (coupon.getDiscount() / 100.0) * activeOrder.getTotalAmount();
        double netAmount = activeOrder.getTotalAmount() - discountAmount;

        activeOrder.setAmount((long) netAmount);
        activeOrder.setDiscount((long) discountAmount);
        activeOrder.setCoupon(coupon);

        orderRepository.save(activeOrder);
        return activeOrder.getOrderDto();
    }

    // Method to check if a coupon is expired
    public boolean couponIsExpired(Coupon coupon) {
        Date currentDate = new Date();
        Date expirationDate = coupon.getExpirationDate();
        return expirationDate != null && currentDate.after(expirationDate);
    }

    // Method to increase the quantity of a product in the cart
    public OrderDto increaseProductQuantity(AddProductInCartDto addProductInCartDto) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.Pending);
        Optional<Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());
        Optional<CartItems> optionalCartItem = cartItemsRepository.findByProductIdAndOrderIdAndUserId(
                optionalProduct.get().getId(), activeOrder.getId(), addProductInCartDto.getUserId());

        if (optionalCartItem.isPresent()) {
            CartItems cartItems = optionalCartItem.get();
            Product product = optionalProduct.get();

            activeOrder.setAmount(activeOrder.getAmount() + product.getPrice());
            activeOrder.setTotalAmount(activeOrder.getTotalAmount() + product.getPrice());
            cartItems.setQuantity(cartItems.getQuantity() + 1);

            if (activeOrder.getCoupon() != null) {
                double discountAmount = (activeOrder.getCoupon().getDiscount() / 100.0) * activeOrder.getTotalAmount();
                double netAmount = activeOrder.getTotalAmount() - discountAmount;

                activeOrder.setAmount((long) netAmount);
                activeOrder.setDiscount((long) discountAmount);
            }

            cartItemsRepository.save(cartItems);
            orderRepository.save(activeOrder);
            return activeOrder.getOrderDto();
        }
        return null;
    }

    // Method to decrease the quantity of a product in the cart
    public OrderDto decreaseProductQuantity(AddProductInCartDto addProductInCartDto) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.Pending);
        Optional<Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());
        Optional<CartItems> optionalCartItem = cartItemsRepository.findByProductIdAndOrderIdAndUserId(
                optionalProduct.get().getId(), activeOrder.getId(), addProductInCartDto.getUserId());

        if (optionalCartItem.isPresent()) {
            CartItems cartItems = optionalCartItem.get();
            Product product = optionalProduct.get();

            activeOrder.setAmount(activeOrder.getAmount() - product.getPrice());
            activeOrder.setTotalAmount(activeOrder.getTotalAmount() - product.getPrice());
            cartItems.setQuantity(cartItems.getQuantity() - 1);

            if (activeOrder.getCoupon() != null) {
                double discountAmount = (activeOrder.getCoupon().getDiscount() / 100.0) * activeOrder.getTotalAmount();
                double netAmount = activeOrder.getTotalAmount() - discountAmount;

                activeOrder.setAmount((long) netAmount);
                activeOrder.setDiscount((long) discountAmount);
            }

            cartItemsRepository.save(cartItems);
            orderRepository.save(activeOrder);
            return activeOrder.getOrderDto();
        }
        return null;
    }

    public OrderDto placedOrder(PlaceOrderDto placeOrderDto) {
        // Fetch the active order for the user with status 'Pending'
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(placeOrderDto.getUserId(), OrderStatus.Pending);
        Optional<User> optionalUser = userRepository.findById(placeOrderDto.getUserId());

        if (optionalUser.isPresent() && activeOrder != null) {
            // Update order details
            activeOrder.setOrderDescription(placeOrderDto.getOrderDescription());
            activeOrder.setAddress(placeOrderDto.getAddress());
            activeOrder.setDate(new Date());
            activeOrder.setOrderStatus(OrderStatus.Placed);
            activeOrder.setTrackingId(UUID.randomUUID());

            // Always set payment as 'Cash on Delivery'
            activeOrder.setPayment("Cash on Delivery");

            // Save the updated order
            orderRepository.save(activeOrder);

            // Create a new pending order for the user
            Order newOrder = new Order();
            newOrder.setAmount(0L);
            newOrder.setTotalAmount(0L);
            newOrder.setDiscount(0L);
            newOrder.setUser(optionalUser.get());
            newOrder.setOrderStatus(OrderStatus.Pending);
            orderRepository.save(newOrder);

            // Convert the active order to OrderDto and return it
            return activeOrder.getOrderDto();
        }

        // If the user is not found or active order is null, return null
        return null;
    }

    // Method to get all placed orders by user ID
    public List<OrderDto> getMyPlacedOrders(Long userId) {
        return orderRepository
                .findByUserIdAndOrderStatusIn(userId, List.of(OrderStatus.Shipped, OrderStatus.Placed, OrderStatus.Delivered))
                .stream()
                .map(Order::getOrderDto)
                .collect(Collectors.toList());
    }

    // Method to search for an order by tracking ID
    public OrderDto searchOrderByTrackingId(UUID trackingId) {
        Optional<Order> optionalOrder = orderRepository.findByTrackingId(trackingId);
        return optionalOrder.map(Order::getOrderDto).orElse(null);
    }

    // Method to set the location of an order
    public void setOrderLocation(Long orderId, Double latitude, Double longitude) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setLatitude(latitude);
            order.setLongitude(longitude);
            orderRepository.save(order);
        }
    }

    public void setOrderLocationByAddress(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            String address = order.getAddress();

            // Make a request to the Google Maps Geocoding API
            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=AIzaSyDtoHqZC-cUQoaw3Coq6BeTeFFPPc6SCgo";

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            try {
                JSONObject json = new JSONObject(response);
                if ("OK".equals(json.getString("status"))) {
                    JSONObject location = json.getJSONArray("results")
                                              .getJSONObject(0)
                                              .getJSONObject("geometry")
                                              .getJSONObject("location");

                    Double latitude = location.getDouble("lat");
                    Double longitude = location.getDouble("lng");

                    // Set the latitude and longitude to the order
                    order.setLatitude(latitude);
                    order.setLongitude(longitude);
                    orderRepository.save(order);
                } else {
                    // Handle the error
                    System.out.println("Error in Geocoding: " + json.getString("status"));
                }
            } catch (JSONException e) {
                // Handle JSON parsing exceptions
                System.out.println("Failed to parse the response: " + e.getMessage());
            }
        }
    }
}