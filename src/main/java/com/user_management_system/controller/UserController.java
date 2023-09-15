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

import com.user_management_system.dto.UserRequest;
import com.user_management_system.entity.User;
import com.user_management_system.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/")
	public String welcome() {
		return "Welcome User!!";
	}
	
//	@PreAuthorize("hasAuthority('ADMIN','USER')")
	@PostMapping("/user-roles/{userRole}/users")
	public ResponseEntity<User> saveUser(@RequestBody UserRequest userRequest, @PathVariable String userRole) {
		return userService.saveUser(userRequest, userRole);
	}
	
	@PreAuthorize("hasAuthority('USER')")
	@GetMapping("/user/one")
	public String userHome() {
		return "This is User 1 Dashboard!!";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/users")
	public ResponseEntity<List<User>> adminHome2() {
		return userService.getUsers();
	}
	
	@PreAuthorize("hasAuthority('USER')")
	@GetMapping("/user/two")
	public String userHome2() {
		return "This is User 2 Dashboard!!";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/admin/three")
	public String adminHome3() {
		return "This is Admin 3 Dashboard!!";
	}
	
	@PreAuthorize("hasAuthority('USER')")
	@GetMapping("/user/three")
	public String userHome3() {
		return "This is User 3 Dashboard!!";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/admin/four")
	public String adminHome4() {
		return "This is Admin 4 Dashboard!!";
	}
	
	@PreAuthorize("hasAuthority('USER')")
	@GetMapping("/user/four")
	public String userHome4() {
		return "This is User 4 Dashboard!!";
	}

}
