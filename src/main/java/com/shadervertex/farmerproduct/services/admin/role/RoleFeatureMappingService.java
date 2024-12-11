package com.shadervertex.farmerproduct.services.admin.role;

import com.shadervertex.farmerproduct.dto.RoleFeatureMappingDto;
import com.shadervertex.farmerproduct.model.Feature;
import com.shadervertex.farmerproduct.model.Role;
import com.shadervertex.farmerproduct.model.RoleFeatureMapping;
import com.shadervertex.farmerproduct.repository.RoleFeatureMappingRepository;
import com.shadervertex.farmerproduct.repository.RoleRepository;
import com.shadervertex.farmerproduct.repository.FeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleFeatureMappingService {

    @Autowired
    private RoleFeatureMappingRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private FeatureRepository featureRepository;

    public RoleFeatureMappingDto createMapping(RoleFeatureMappingDto dto) {
        // Check if the role exists
        Role role = roleRepository.findById(dto.getRoleId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found with id: " + dto.getRoleId()));

        // Check if the feature exists
        Feature feature = featureRepository.findById(dto.getFeatureId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Feature not found with id: " + dto.getFeatureId()));

        // Check if the role-feature mapping already exists
        if (repository.existsByRoleAndFeature(role, feature)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Mapping already exists for this role and feature.");
        }

        // Create and save the RoleFeatureMapping entity
        RoleFeatureMapping entity = RoleFeatureMapping.builder()
            .role(role)
            .feature(feature)
            .build();

        RoleFeatureMapping savedEntity = repository.save(entity);

        // Return DTO with the saved entity data
        return RoleFeatureMappingDto.builder()
            .id(savedEntity.getId())
            .roleId(savedEntity.getRole().getId())
            .featureId(savedEntity.getFeature().getId())
            .build();
    }

    public List<RoleFeatureMappingDto> getAllMappings() {
        List<RoleFeatureMapping> entities = repository.findAll();
        return entities.stream().map(entity -> RoleFeatureMappingDto.builder()
            .id(entity.getId())
            .roleId(entity.getRole().getId())
            .featureId(entity.getFeature().getId())
            .build()).collect(Collectors.toList());
    }

    public RoleFeatureMappingDto updateMapping(Long id, RoleFeatureMappingDto dto) {
        // Find the existing mapping by id
        RoleFeatureMapping existingMapping = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mapping not found with id: " + id));

        // Check if the role exists
        Role role = roleRepository.findById(dto.getRoleId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found with id: " + dto.getRoleId()));

        // Check if the feature exists
        Feature feature = featureRepository.findById(dto.getFeatureId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Feature not found with id: " + dto.getFeatureId()));

        // Check if a different mapping with the same role-feature pair exists
        if (repository.existsByRoleAndFeature(role, feature) && !existingMapping.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Mapping already exists for this role and feature.");
        }

        // Update the mapping fields
        existingMapping.setRole(role);
        existingMapping.setFeature(feature);

        // Save the updated mapping
        RoleFeatureMapping updatedMapping = repository.save(existingMapping);

        // Return the updated mapping in DTO format
        return RoleFeatureMappingDto.builder()
            .id(updatedMapping.getId())
            .roleId(updatedMapping.getRole().getId())
            .featureId(updatedMapping.getFeature().getId())
            .build();
    }

    public void deleteMapping(Long id) {
        // Check if the mapping exists
        repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mapping not found with id: " + id));

        // Delete the mapping
        repository.deleteById(id);
    }
}