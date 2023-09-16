package com.user_management_system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.user_management_system.dto.AuthRequest;
import com.user_management_system.dto.AuthResponse;
import com.user_management_system.dto.UserRequest;
import com.user_management_system.entity.User;
import com.user_management_system.entity.UserRole;
import com.user_management_system.exception.UserNameNotFoundException;
import com.user_management_system.repository.UserRepo;
import com.user_management_system.security.JwtService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	public ResponseEntity<AuthResponse> getAuthenticatedToken(AuthRequest authRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUserEmail(), authRequest.getUserPassword()));
		if (authentication.isAuthenticated()) {
			String token = jwtService.generateToken(authRequest.getUserEmail());
			System.out.println("Token = "+token);
			return new ResponseEntity<AuthResponse>(new AuthResponse(token), HttpStatus.OK);
		} else
			throw new UserNameNotFoundException("Failed to Authenticate the user!!");

	}

	public ResponseEntity<User> saveUser(UserRequest userRequest, String userRole) {

		UserRole role = UserRole.valueOf(userRole.toUpperCase());
		User user = User.builder().userName(userRequest.getUserName()).userEmail(userRequest.getUserEmail())
				.userRole(role).userPassword(passwordEncoder.encode(userRequest.getUserPassword())).build();
		userRepo.save(user);
		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}

	public ResponseEntity<List<User>> getUsers() {
		return new ResponseEntity<List<User>>(userRepo.findAll(), HttpStatus.FOUND);
	}

}
