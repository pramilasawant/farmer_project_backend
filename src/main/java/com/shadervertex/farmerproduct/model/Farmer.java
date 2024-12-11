package com.shadervertex.farmerproduct.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.Builder;
@Entity
@Data
@NoArgsConstructor
@Builder // Add this annotation
@AllArgsConstructor
@Table(name = "farmers")

public class Farmer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "farm_details", nullable = false)
    private String farmDetails;
}

