package com.user_management_system.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@SuppressWarnings("serial")
@Getter
@AllArgsConstructor
public class CustomJwtExpiredException extends RuntimeException {
private String message;
}
