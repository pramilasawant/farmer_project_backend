package com.shadervertex.farmerproduct.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PlaceOrderDto {
	
	private Long userId;
	
	private String address;
	
	private String orderDescription;
	
	private String paymentMethod;  // Renamed from 'payment' to 'paymentMethod'
}

