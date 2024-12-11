package com.shadervertex.farmerproduct.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shadervertex.farmerproduct.dto.RoleFeatureMappingDto;
import com.shadervertex.farmerproduct.services.admin.role.RoleFeatureMappingService;

@RestController
@RequestMapping("/api/admin/rolefeaturemapping")
public class RoleFeatureMappingController {

    @Autowired
    private RoleFeatureMappingService mappingService;

    // POST mapping to create a new role-feature mapping
    @PostMapping("/create")
    public ResponseEntity<?> createMapping(@RequestBody RoleFeatureMappingDto mappingDto) {
        try {
            if (mappingDto.getRoleId() == null || mappingDto.getFeatureId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role ID and Feature ID must not be null.");
            }

            RoleFeatureMappingDto createdMapping = mappingService.createMapping(mappingDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMapping);
        } catch (IllegalArgumentException e) {
            // Handle bad data and return detailed exception message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data: " + e.getMessage());
        } catch (Exception e) {
            // Catch other general exceptions and return their details in response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating mapping: " + e.getMessage());
        }
    }

    // GET mapping to fetch all role-feature mappings
    @GetMapping("/list")
    public ResponseEntity<?> getAllMappings() {
        try {
            List<RoleFeatureMappingDto> mappings = mappingService.getAllMappings();
            return ResponseEntity.ok(mappings);
        } catch (Exception e) {
            // Return exception details if something goes wrong
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching mappings: " + e.getMessage());
        }
    }

    // PUT mapping to update a role-feature mapping by ID
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> updateMapping(@PathVariable Long id, @RequestBody RoleFeatureMappingDto mappingDto) {
        try {
            if (mappingDto.getRoleId() == null || mappingDto.getFeatureId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role ID and Feature ID must not be null.");
            }

            RoleFeatureMappingDto updatedMapping = mappingService.updateMapping(id, mappingDto);
            return ResponseEntity.ok(updatedMapping);
        } catch (IllegalArgumentException e) {
            // Handle specific validation errors
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data: " + e.getMessage());
        } catch (Exception e) {
            // Return details of any other errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating mapping: " + e.getMessage());
        }
    }

    // DELETE mapping to remove a role-feature mapping by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMapping(@PathVariable Long id) {
        try {
            mappingService.deleteMapping(id);
            return ResponseEntity.ok("Mapping deleted successfully.");
        } catch (IllegalArgumentException e) {
            // Handle invalid ID errors
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID: " + e.getMessage());
        } catch (Exception e) {
            // Return exception details in case of error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting mapping: " + e.getMessage());
        }
    }
}
