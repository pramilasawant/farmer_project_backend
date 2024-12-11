package com.shadervertex.farmerproduct.services;

import com.shadervertex.farmerproduct.dto.FarmerDto;
import com.shadervertex.farmerproduct.model.Farmer;
import com.shadervertex.farmerproduct.model.User; // Ensure you import the User class
import com.shadervertex.farmerproduct.repository.FarmerRepository;
import com.shadervertex.farmerproduct.repository.UserRepository; // Import the User repository
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FarmerService {

    private final FarmerRepository farmerRepository;
    private final UserRepository userRepository; // Inject the UserRepository
    private final BCryptPasswordEncoder passwordEncoder;

    public FarmerDto signUpFarmer(FarmerDto farmerDto) {
        // Check if the farmer already exists
        if (farmerRepository.existsByEmail(farmerDto.getEmail())) {
            throw new IllegalArgumentException("Farmer with this email already exists");
        }

        // Encrypt password
        String encodedPassword = passwordEncoder.encode(farmerDto.getPassword());

        // Create User and get the ID
        Long userId = createUserAndGetId(farmerDto, encodedPassword); // Pass the encoded password

        // Create Farmer entity from DTO
        Farmer farmer = new Farmer();
        farmer.setUserId(userId);
        farmer.setName(farmerDto.getName());
        farmer.setEmail(farmerDto.getEmail());
        farmer.setPassword(encodedPassword); // Store encrypted password
        farmer.setContactNumber(farmerDto.getContactNumber());
        farmer.setAddress(farmerDto.getAddress());
        farmer.setFarmDetails(farmerDto.getFarmDetails());

        // Save Farmer to the database
        Farmer savedFarmer = farmerRepository.save(farmer);

        // Convert saved Farmer entity back to DTO
        return convertToDto(savedFarmer);
    }

    private FarmerDto convertToDto(Farmer farmer) {
        return new FarmerDto(
            farmer.getId(),
            farmer.getUserId(),
            farmer.getName(),
            farmer.getEmail(),
            null, // Typically, you wouldn’t return the password in a DTO
            farmer.getContactNumber(),
            farmer.getAddress(),
            farmer.getFarmDetails()
        );
    }

    private Long createUserAndGetId(FarmerDto farmerDto, String encodedPassword) {
        // Create a new User entity
        User newUser = new User();
        newUser.setEmail(farmerDto.getEmail());
        newUser.setPassword(encodedPassword); // Set the encrypted password
        newUser.setName(farmerDto.getName()); // Set other necessary fields

        // Save the user to the database
        User savedUser = userRepository.save(newUser);

        // Return the generated ID
        return savedUser.getId(); // Ensure User has an ID after save
    }
}

