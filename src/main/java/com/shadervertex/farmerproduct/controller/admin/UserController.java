package com.shadervertex.farmerproduct.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.shadervertex.farmerproduct.dto.UserDto;
import com.shadervertex.farmerproduct.enums.UserRole;
import com.shadervertex.farmerproduct.model.User;
import com.shadervertex.farmerproduct.services.admin.user.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class UserController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserService userService;

    // Helper method to hash passwords
    private String hashPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    @PostMapping("/addUser")
    public ResponseEntity<?> addUser(
            @RequestParam("name") String name,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("role") String role,
            @RequestParam("phoneNo") String phoneNo,
            @RequestParam("address_line1") String addressLine1,
            @RequestParam("address_line2") String addressLine2,
            @RequestParam("pincode1") String pincode1,
            @RequestParam("pincode2") String pincode2,
            @RequestParam("identityProof") MultipartFile identityProof,
            @RequestParam("img") MultipartFile img) {
        try {
            // Hash the password before saving the user
            String hashedPassword = hashPassword(password);

            UserRole userRole = UserRole.valueOf(role.toUpperCase());

            User user = User.builder()
                    .name(name)
                    .password(hashedPassword) // Store the hashed password
                    .email(email)
                    .role(userRole)
                    .phoneNo(phoneNo)
                    .address_line1(addressLine1)
                    .address_line2(addressLine2)
                    .pincode1(pincode1)
                    .pincode2(pincode2)
                    .identityProof(identityProof.getBytes())
                    .img(img.getBytes())
                    .build();

            User savedUser = userService.saveUser(user);
            UserDto savedUserDto = userService.convertToDto(savedUser);
            return ResponseEntity.ok(savedUserDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid role value.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing files.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserDto> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred while retrieving users.");
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            UserDto userDto = userService.getUserById(id);
            if (userDto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }
            userDto.setPassword(null); // Exclude password from the response
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred while retrieving the user.");
        }
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam("email") String email,
            @RequestParam("role") String role,
            @RequestParam("phoneNo") String phoneNo,
            @RequestParam("address_line1") String addressLine1,
            @RequestParam("address_line2") String addressLine2,
            @RequestParam("pincode1") String pincode1,
            @RequestParam("pincode2") String pincode2,
            @RequestParam(value = "identityProof", required = false) MultipartFile identityProof,
            @RequestParam(value = "img", required = false) MultipartFile img) {
        try {
            UserDto existingUserDto = userService.getUserById(id);
            if (existingUserDto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }

            User existingUser = userService.convertToEntity(existingUserDto);

            if (password != null && !password.isEmpty()) {
                existingUser.setPassword(hashPassword(password));
            }

            existingUser.setName(name);
            existingUser.setEmail(email);
            existingUser.setRole(UserRole.valueOf(role.toUpperCase()));
            existingUser.setPhoneNo(phoneNo);
            existingUser.setAddress_line1(addressLine1);
            existingUser.setAddress_line2(addressLine2);
            existingUser.setPincode1(pincode1);
            existingUser.setPincode2(pincode2);

            if (identityProof != null && !identityProof.isEmpty()) {
                existingUser.setIdentityProof(identityProof.getBytes());
            }
            if (img != null && !img.isEmpty()) {
                existingUser.setImg(img.getBytes());
            }

            User updatedUser = userService.saveUser(existingUser);
            UserDto updatedUserDto = userService.convertToDto(updatedUser);
            return ResponseEntity.ok(updatedUserDto);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing files.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid role value.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            UserDto userDto = userService.getUserById(id);
            if (userDto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred while deleting the user.");
        }
    }
}
