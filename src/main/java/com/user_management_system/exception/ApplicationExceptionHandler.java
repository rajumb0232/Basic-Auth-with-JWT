package com.user_management_system.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.user_management_system.util.ErrorStructure;

@RestControllerAdvice
public class ApplicationExceptionHandler {

	@Autowired
	private ErrorStructure error;
	
	public ResponseEntity<ErrorStructure> UserNameNotFound(UsernameNotFoundException ex){
		error.setMessage(ex.getMessage());
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setRootCause("User with the Requested UserName is not Found!!");
		return new ResponseEntity<ErrorStructure>(error, HttpStatus.NOT_FOUND);
	}
}
