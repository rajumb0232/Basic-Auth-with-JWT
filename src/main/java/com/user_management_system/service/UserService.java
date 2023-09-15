package com.user_management_system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.user_management_system.dto.UserRequest;
import com.user_management_system.entity.User;
import com.user_management_system.entity.UserRole;
import com.user_management_system.repository.UserRepo;

@Service
public class UserService {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public ResponseEntity<User> saveUser(UserRequest userRequest, String userRole) {

		UserRole role = UserRole.valueOf(userRole.toUpperCase());

		User user = new User();
		user.setUserName(userRequest.getUserName());
		user.setUserEmail(userRequest.getUserEmail());
		user.setUserPassword(passwordEncoder.encode(userRequest.getUserPassword()));
		user.setUserRole(role);
		userRepo.save(user);
		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}
	
	
	public ResponseEntity<List<User>> getUsers(){
		return new ResponseEntity<List<User>>(userRepo.findAll(), HttpStatus.FOUND);
	}
}
