package com.shadervertex.farmerproduct.services.auth;

import com.shadervertex.farmerproduct.dto.SignupRequest;
import com.shadervertex.farmerproduct.dto.UserDto;

public interface AuthService {
	UserDto createUser(SignupRequest signupRequest);

	Boolean hasUserWithEmail(String email);

	void createAdminAccount();
}