package com.user_management_system.dto;

import lombok.Getter;

@Getter
public class AuthRequest {
	private String userEmail;
	private String userPassword;
}
