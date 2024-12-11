package com.shadervertex.farmerproduct.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class FarmerProductRequestDto {
    private Long id;
    private String name;
    private Long price;
    private String description;
    private byte[] img;
    private Long categoryId;
    private String categoryName;
    private Boolean isApproved;
    private Long submittedBy;
}

