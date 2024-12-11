package com.shadervertex.farmerproduct.services.auth;

import com.shadervertex.farmerproduct.dto.FarmerDto;
import com.shadervertex.farmerproduct.dto.SignupRequest;
import com.shadervertex.farmerproduct.dto.UserDto;
import com.shadervertex.farmerproduct.enums.OrderStatus;
import com.shadervertex.farmerproduct.enums.UserRole;
import com.shadervertex.farmerproduct.model.Farmer;
import com.shadervertex.farmerproduct.model.Order;
import com.shadervertex.farmerproduct.model.User;
import com.shadervertex.farmerproduct.repository.FarmerRepository;
import com.shadervertex.farmerproduct.repository.OrderRepository;
import com.shadervertex.farmerproduct.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final FarmerRepository farmerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final OrderRepository orderRepository;

    @Override
    public UserDto createUser(SignupRequest signupRequest) {
        // Hash the user's password before saving it to the database
        String hashedPassword = bCryptPasswordEncoder.encode(signupRequest.getPassword());

        // Create and save a new user with a default role of CUSTOMER
        User createdUser = userRepository.save(User.builder()
                .email(signupRequest.getEmail())
                .name(signupRequest.getName())
                .password(hashedPassword)
                .role(UserRole.CUSTOMER)   // Default role is CUSTOMER
                .build());

        // Create a new order associated with the user
        Order order = new Order();
        order.setAmount(0L);
        order.setTotalAmount(0L);
        order.setDiscount(0L);
        order.setUser(createdUser);
        order.setOrderStatus(OrderStatus.Pending);
        orderRepository.save(order);

        // Create and return the UserDto for the created user
        UserDto userDto = new UserDto();
        userDto.setId(createdUser.getId());
        return userDto;
    }

    public FarmerDto loginFarmer(String email, String password) {
        // Find farmer by email, throw exception if not found
        Farmer farmer = farmerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Farmer email or password"));

        // Validate the password, throw exception if incorrect
        if (bCryptPasswordEncoder.matches(password, farmer.getPassword())) {
            return convertToDto(farmer); // Convert the farmer entity to DTO and return
        } else {
            throw new IllegalArgumentException("Invalid Farmer email or password");
        }
    }

    private FarmerDto convertToDto(Farmer farmer) {
        return new FarmerDto(
                farmer.getId(),
                farmer.getUserId(),
                farmer.getName(),
                farmer.getEmail(),
                farmer.getPassword(),
                farmer.getContactNumber(),
                farmer.getAddress(),
                farmer.getFarmDetails()
        );
    }

    public FarmerDto signUpFarmer(FarmerDto farmerDto) {
        // Check if the farmer already exists
        if (farmerRepository.existsByEmail(farmerDto.getEmail())) {
            throw new IllegalArgumentException("Farmer with this email already exists");
        }

        // Encrypt password
        String encodedPassword = bCryptPasswordEncoder.encode(farmerDto.getPassword());

        // Create User and get the ID
        Long userId = createUserAndGetId(farmerDto, encodedPassword); // Pass the encoded password

        // Create Farmer entity from DTO
        Farmer farmer = Farmer.builder()
                .userId(userId) // Ensure userId is set here
                .name(farmerDto.getName())
                .email(farmerDto.getEmail())
                .password(encodedPassword) // Store encrypted password
                .contactNumber(farmerDto.getContactNumber())
                .address(farmerDto.getAddress())
                .farmDetails(farmerDto.getFarmDetails())
                .build();

        // Save Farmer to the database
        Farmer savedFarmer = farmerRepository.save(farmer);

        // Convert saved Farmer entity back to DTO
        return convertToDto(savedFarmer);
    }

    private Long createUserAndGetId(FarmerDto farmerDto, String encodedPassword) {
        User newUser = User.builder()
                .email(farmerDto.getEmail())
                .password(encodedPassword) // Set the encrypted password
                .name(farmerDto.getName()) // Set other necessary fields
                .role(UserRole.FARMER) // Assign the FARMER role
                .build();

        // Save the user to the database
        User savedUser = userRepository.save(newUser);

        // Return the generated ID
        return savedUser.getId(); // Ensure User has an ID after save
    }

    // Check if a user with the given email already exists
    @Override
    public Boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }

    // Initialize method to perform startup actions
    @PostConstruct
    public void init() {
        log.info("Application started, checking user passwords");
        updatePasswords(); // Ensure all passwords are hashed
        createAdminAccount(); // Create an admin account if one doesn't exist
    }

    // Method to update user passwords, ensuring they are hashed
    public void updatePasswords() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            if (!user.getPassword().startsWith("$2a$")) { // If password isn't hashed, hash and save it
                String hashedPassword = bCryptPasswordEncoder.encode(user.getPassword());
                user.setPassword(hashedPassword);
                userRepository.save(user);
            }
        }
    }

    // Method to create an admin account if one doesn't already exist
    public void createAdminAccount() {
        log.info("Running application for the first time, creating an Admin account with default info");
        Optional<User> adminAccountUser = userRepository.findByRole(UserRole.ADMIN);

        if (adminAccountUser.isEmpty()) {
            log.info("Admin account created with email: admin@gmail.com and password: @Admin123");
            userRepository.save(
                User.builder()
                        .email("admin@gmail.com")
                        .name("admin")
                        .role(UserRole.ADMIN)
                        .password(bCryptPasswordEncoder.encode("@Admin123"))
                        .build()
            );
        }
    }
}

