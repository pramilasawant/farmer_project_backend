package com.shadervertex.farmerproduct.services.admin.coupon;

import java.util.List;

import com.shadervertex.farmerproduct.model.Coupon;

public interface AdminCouponService {
	Coupon createCoupon(Coupon coupon);
	List<Coupon> getAllCoupon();
}