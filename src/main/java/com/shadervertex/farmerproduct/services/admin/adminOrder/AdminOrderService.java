package com.shadervertex.farmerproduct.services.admin.adminOrder;

import java.util.List;

import com.shadervertex.farmerproduct.dto.AnalyticsResponse;
import com.shadervertex.farmerproduct.dto.OrderDto;

public interface AdminOrderService {
    List<OrderDto> getAllPlacedOrders();
    OrderDto changeOrderStatus(Long orderId, String status);
    AnalyticsResponse calculateAnalytics();
    Long getTotalOrdersForMonths(int month, int year);
    Long getTotalEarningsForMonth(int month, int year);
}