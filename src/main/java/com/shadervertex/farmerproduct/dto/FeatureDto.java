package com.shadervertex.farmerproduct.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureDto {
    private Long id;
    private String featuresname;
    private String featuresdescription;
}
