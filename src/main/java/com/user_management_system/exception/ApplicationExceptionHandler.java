package com.user_management_system.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.user_management_system.util.ErrorStructure;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApplicationExceptionHandler {
//
//	@Autowired
//	private ErrorStructure error;

	@ExceptionHandler(CustomJwtExpiredException.class)
	public ResponseEntity<ErrorStructure> JwtTokenExpired(CustomJwtExpiredException ex) {
		System.err.println("--------------****************____________________*****************-------------");
		ErrorStructure error = new ErrorStructure();
		error.setMessage(ex.getMessage());
		error.setStatus(HttpStatus.FORBIDDEN.value());
		error.setRootCause("User Token is Expired, login back to continue!!");
		return new ResponseEntity<ErrorStructure>(error, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorStructure> UserNameNotFound(BadCredentialsException ex) {
		ErrorStructure error = new ErrorStructure();
		error.setMessage("Failed to Authenticate the User!!");
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setRootCause("Invalid email or password. Please check your credentials!!");
		return new ResponseEntity<ErrorStructure>(error, HttpStatus.NOT_FOUND);
	}

}
