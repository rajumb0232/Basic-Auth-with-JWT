package com.user_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user_management_system.entity.RefreshToken;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, String> {

	public RefreshToken findByRefreshToken(String refreshToken);

}
