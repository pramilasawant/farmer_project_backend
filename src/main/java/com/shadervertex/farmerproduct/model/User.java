package com.shadervertex.farmerproduct.model;

import com.shadervertex.farmerproduct.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.*;
@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String password;
    private String email;

    @Enumerated(EnumType.STRING)  // Store enum values as strings
    private UserRole role;

    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] img;

    @Transient  // This field is not persisted in the database
    private String confirmPassword;

    private String phoneNo;  // New field for phone number
   
    @Column(name = "address_line1", columnDefinition = "TEXT")
    private String address_line1;  // Address Line 1

    @Column(name = "pincode1", length = 10)
    private String pincode1;  // Pincode 1

    @Column(name = "address_line2", columnDefinition = "TEXT")
    private String address_line2;  // Address Line 2

    @Column(name = "pincode2", length = 10)
    private String pincode2;  // Pincode 2

    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] identityProof;  // New field for identity proof
}
