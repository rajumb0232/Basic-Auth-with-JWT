package com.user_management_system.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.user_management_system.util.ErrorStructure;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class ApplicationExceptionHandler {

	@Autowired
	private ErrorStructure error;

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorStructure> UserNameNotFound(BadCredentialsException ex) {
		log.error(ex.getMessage()+" : Invalid email or password. Please check your credentials!!");
		error.setMessage("Failed to Authenticate the User!!");
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setRootCause("Invalid email or password. Please check your credentials!!");
		return new ResponseEntity<ErrorStructure>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public void deniedAccessDenied(AccessDeniedException ex) {
		log.error(ex.getMessage()+" : User is not authorized!!");
	}
	
	@ExceptionHandler(UserNotFoundWithRefreshTokenException.class)
	public ResponseEntity<ErrorStructure> handleUserNotFoundWithRefreshTokenException(UserNotFoundWithRefreshTokenException ex){
		log.error(ex.getMessage()+" : No user found associated with the RefreshToken!!");
		error.setMessage(ex.getMessage());
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setRootCause("No user found associated with the RefreshToken.");
		return new ResponseEntity<ErrorStructure>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(RefreshTokenExpiredException.class)
	public ResponseEntity<ErrorStructure> handleRefreshTokenExpiredException(RefreshTokenExpiredException ex){
		log.error(ex.getMessage()+" : The provided Refersh token is expired!!");
		error.setMessage(ex.getMessage());
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setRootCause("The provided Refersh token is expired.");
		return new ResponseEntity<ErrorStructure>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InvalidRefreshTokenException.class)
	public ResponseEntity<ErrorStructure> handleInvalidRefreshTokenException(InvalidRefreshTokenException ex){
		log.error(ex.getMessage()+" : Provided RefreshToken is not found or not associated with the user!!");
		error.setMessage(ex.getMessage());
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setRootCause("Provided RefreshToken is not found or not associated with the user.");
		return new ResponseEntity<ErrorStructure>(error, HttpStatus.BAD_REQUEST);
	}

}
