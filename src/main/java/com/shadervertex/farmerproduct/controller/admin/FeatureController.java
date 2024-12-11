package com.shadervertex.farmerproduct.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shadervertex.farmerproduct.dto.FeatureDto;
import com.shadervertex.farmerproduct.services.admin.role.FeatureService;

@RestController
@RequestMapping("/api/admin/feature")
public class FeatureController {

    @Autowired
    private FeatureService featureService;

    // POST mapping to create a new feature
    @PostMapping("/create")
    public ResponseEntity<?> createFeature(@RequestBody FeatureDto featureDto) {
        try {
            FeatureDto createdFeature = featureService.createFeature(featureDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFeature);
        } catch (Exception e) {
            // Log the error details and return a 500 Internal Server Error response
            System.err.println("Error creating feature: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create feature: " + e.getMessage());
        }
    }

    // GET mapping to fetch all features
    @GetMapping("/list")
    public ResponseEntity<?> getAllFeatures() {
        try {
            List<FeatureDto> featureList = featureService.getAllFeatures();
            return ResponseEntity.ok(featureList);
        } catch (Exception e) {
            // Log the error details and return a 500 Internal Server Error response
            System.err.println("Error fetching features: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch features: " + e.getMessage());
        }
    }
}
