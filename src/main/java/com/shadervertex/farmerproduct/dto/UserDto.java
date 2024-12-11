package com.shadervertex.farmerproduct.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String name;
    private String password;
    private String email;
    private String role;        // Assuming role is still a String in the DTO
    private String phoneNo;     // Field for phone number
    private String address_line1; // Field for Address Line 1
    private String pincode1;    // Field for Pincode 1
    private String address_line2; // Field for Address Line 2
    private String pincode2;    // Field for Pincode 2
    private byte[] img;         // Image field
    private byte[] identityProof; // Field for identity proof
}

