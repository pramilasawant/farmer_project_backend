package com.shadervertex.farmerproduct.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class FarmerDto {

    private Long id;
    private Long userId;
    private String name;
    private String email;
    private String password; // Typically, this would be hashed in practice
    private String contactNumber;
    private String address;
    private String farmDetails;
}

