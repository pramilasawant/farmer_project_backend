package com.shadervertex.farmerproduct.controller.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shadervertex.farmerproduct.dto.AnalyticsResponse;
import com.shadervertex.farmerproduct.dto.OrderDto;
import com.shadervertex.farmerproduct.services.admin.adminOrder.AdminOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminOrderController {

	private final AdminOrderService adminOrderService;

	@GetMapping("/placedOrders")
	public ResponseEntity<List<OrderDto>> getAllPlacedOrders() {
		return ResponseEntity.ok(adminOrderService.getAllPlacedOrders());
	}

	// TODO : fix ( use @PutMapping which is more semantically correct for updating resources )
	@GetMapping("/order/{orderId}/{status}")
	public ResponseEntity<?> changeOrderStatus(@PathVariable Long orderId, @PathVariable String status) {
		OrderDto orderDto = adminOrderService.changeOrderStatus(orderId, status);
		if (orderDto == null)
			return ResponseEntity.badRequest().body("Something Went Wrong!!");

		return ResponseEntity.ok(orderDto);
	}
	
	@GetMapping("/order/analytics")
	public ResponseEntity<AnalyticsResponse> getAnalytics() {
		return ResponseEntity.ok(adminOrderService.calculateAnalytics());
	}

}

