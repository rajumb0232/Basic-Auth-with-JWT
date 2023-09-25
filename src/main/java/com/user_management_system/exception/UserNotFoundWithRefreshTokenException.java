package com.user_management_system.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@SuppressWarnings("serial")
@AllArgsConstructor
@Getter
public class UserNotFoundWithRefreshTokenException extends RuntimeException {
	private String message;
}
