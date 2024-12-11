package com.shadervertex.farmerproduct.services.admin.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.shadervertex.farmerproduct.dto.UserDto;
import com.shadervertex.farmerproduct.enums.UserRole;
import com.shadervertex.farmerproduct.model.User;
import com.shadervertex.farmerproduct.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder; // Injecting BCryptPasswordEncoder

    // Method to save a User entity
    public User saveUser(User user) {
        // Encrypt the password if it's provided
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    // Get all users and convert to DTOs
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Get a user by ID and convert to DTO
    public UserDto getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(this::convertToDto).orElse(null);
    }

    // Delete a user by ID
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Convert User entity to UserDto (without password)
    public UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .phoneNo(user.getPhoneNo())
                .address_line1(user.getAddress_line1())
                .pincode1(user.getPincode1())
                .address_line2(user.getAddress_line2())
                .pincode2(user.getPincode2())
                .img(user.getImg())
                .identityProof(user.getIdentityProof())
                .build();
    }

    // Convert UserDto to User entity (without password update)
    public User convertToEntity(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .role(UserRole.valueOf(userDto.getRole()))
                .phoneNo(userDto.getPhoneNo())
                .address_line1(userDto.getAddress_line1())
                .pincode1(userDto.getPincode1())
                .address_line2(userDto.getAddress_line2())
                .pincode2(userDto.getPincode2())
                .img(userDto.getImg())
                .identityProof(userDto.getIdentityProof())
                .build();
    }
}