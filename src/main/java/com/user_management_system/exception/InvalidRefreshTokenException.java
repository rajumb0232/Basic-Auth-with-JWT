package com.user_management_system.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@SuppressWarnings("serial")
@Getter
@AllArgsConstructor
public class InvalidRefreshTokenException extends RuntimeException {
	private String message;
}
