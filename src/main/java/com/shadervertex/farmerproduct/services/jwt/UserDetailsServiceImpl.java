package com.shadervertex.farmerproduct.services.jwt;

import java.util.ArrayList;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.shadervertex.farmerproduct.model.User;
import com.shadervertex.farmerproduct.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> optionalUser = userRepository.findFirstByEmail(username);
		if (optionalUser.isEmpty())
			throw new UsernameNotFoundException("username not found", null);
		return new org.springframework.security.core.userdetails.User(optionalUser.get().getEmail(),
				optionalUser.get().getPassword(), new ArrayList<>());
	}
}