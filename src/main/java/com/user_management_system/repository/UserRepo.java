package com.user_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user_management_system.entity.User;

public interface UserRepo extends JpaRepository<User, Integer> {
	
	public User findByUserEmail(String userEmail);
}
