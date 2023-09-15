package com.user_management_system.util;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class ErrorStructure {
	private int status;
	private String message;
	private String rootCause;
}
