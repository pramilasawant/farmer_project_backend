

package com.shadervertex.farmerproduct.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleFeatureMappingDto {
    private Long id;
    private Long roleId;
    private Long featureId;
}
