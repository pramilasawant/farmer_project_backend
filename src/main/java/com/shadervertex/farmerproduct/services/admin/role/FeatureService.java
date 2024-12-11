package com.shadervertex.farmerproduct.services.admin.role;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shadervertex.farmerproduct.dto.FeatureDto;
import com.shadervertex.farmerproduct.model.Feature;
import com.shadervertex.farmerproduct.repository.FeatureRepository;

@Service
public class FeatureService {
    @Autowired
    private FeatureRepository featureRepository;

    public FeatureDto createFeature(FeatureDto featureDto) {
        Feature feature = Feature.builder()
                                 .featuresname(featureDto.getFeaturesname())
                                 .featuresdescription(featureDto.getFeaturesdescription())
                                 .build();
        
        Feature savedFeature = featureRepository.save(feature);
        
        return FeatureDto.builder()
                         .id(savedFeature.getId()) // Now returns Long
                         .featuresname(savedFeature.getFeaturesname())
                         .featuresdescription(savedFeature.getFeaturesdescription())
                         .build();
    }

    public List<FeatureDto> getAllFeatures() {
        return featureRepository.findAll().stream()
                                .map(feature -> FeatureDto.builder()
                                                          .id(feature.getId()) // Now returns Long
                                                          .featuresname(feature.getFeaturesname())
                                                          .featuresdescription(feature.getFeaturesdescription())
                                                          .build())
                                .collect(Collectors.toList());
    }
}
