package com.shadervertex.farmerproduct.services.admin.role;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shadervertex.farmerproduct.dto.RoleDto;
import com.shadervertex.farmerproduct.model.Role;
import com.shadervertex.farmerproduct.repository.RoleRepository;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public RoleDto createRole(RoleDto roleDto) {
        Role role = Role.builder()
                        .rolename(roleDto.getRolename()) // Corrected field
                        .roledescription(roleDto.getRoledescription()) // Corrected field
                        .build();
        
        Role savedRole = roleRepository.save(role);
        
        return RoleDto.builder()
                      .id(savedRole.getId()) // Long type
                      .rolename(savedRole.getRolename()) // Corrected field
                      .roledescription(savedRole.getRoledescription()) // Corrected field
                      .build();
    }

    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream()
                             .map(role -> RoleDto.builder()
                                                 .id(role.getId()) // Long type
                                                 .rolename(role.getRolename()) // Corrected field
                                                 .roledescription(role.getRoledescription()) // Corrected field
                                                 .build())
                             .collect(Collectors.toList());
    }
}