package com.user_management_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.user_management_system.dto.AuthRequest;
import com.user_management_system.dto.AuthResponse;
import com.user_management_system.dto.UserRequest;
import com.user_management_system.entity.User;
import com.user_management_system.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;


	@GetMapping("/")
	public String[] welcome() {
		return new String[] { "http://localhost:8080/user-roles/admin/users/register",
				"http://localhost:8080/user-roles/user/users/register", "http://localhost:8080/logout" };
	}

//	@PreAuthorize("hasAuthority('ADMIN','USER')")
	@PostMapping("/user-roles/{userRole}/users/register")
	public ResponseEntity<User> saveUser(@RequestBody UserRequest userRequest, @PathVariable String userRole) {
		return userService.saveUser(userRequest, userRole);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/users")
	public ResponseEntity<List<User>> adminHome2() {
		return userService.getUsers();
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<AuthResponse> getAuthenticatedToken(@RequestBody AuthRequest authRequest){
		return userService.getAuthenticatedToken(authRequest);
	}

}
