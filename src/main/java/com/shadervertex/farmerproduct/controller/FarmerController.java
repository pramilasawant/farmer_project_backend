package com.shadervertex.farmerproduct.controller;

import com.shadervertex.farmerproduct.dto.FarmerDto;
import com.shadervertex.farmerproduct.services.auth.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map; 

@RestController
@RequestMapping("/farmers")
@RequiredArgsConstructor
public class FarmerController {

    private final AuthServiceImpl authService;
    private static final Logger log = LoggerFactory.getLogger(FarmerController.class);

   @PostMapping("/login")
public ResponseEntity<?> loginFarmer(@RequestBody FarmerDto farmerDto) {
    try {
        FarmerDto farmerResponse = authService.loginFarmer(farmerDto.getEmail(), farmerDto.getPassword());
        // Create a success message
        String successMessage = "Login successful!";
        return ResponseEntity.ok().body(Map.of("message", successMessage, "farmer", farmerResponse));
    } catch (Exception e) {
        log.error("Login failed: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed: " + e.getMessage());
    }
}


    @PostMapping("/signup")
    public ResponseEntity<?> signUpFarmer(@RequestBody FarmerDto farmerDto) {
        try {
            FarmerDto savedFarmer = authService.signUpFarmer(farmerDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedFarmer); // 201 Created
        } catch (Exception e) {
            log.error("Sign up failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sign up failed: " + e.getMessage());
        }
    }
}

