package com.user_management_system.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.user_management_system.dto.AuthRequest;
import com.user_management_system.dto.AuthResponse;
import com.user_management_system.dto.RefreshTokenRequest;
import com.user_management_system.dto.UserRequest;
import com.user_management_system.entity.RefreshToken;
import com.user_management_system.entity.User;
import com.user_management_system.entity.UserRole;
import com.user_management_system.exception.InvalidRefreshTokenException;
import com.user_management_system.exception.RefreshTokenExpiredException;
import com.user_management_system.exception.UserNameNotFoundException;
import com.user_management_system.exception.UserNotFoundWithRefreshTokenException;
import com.user_management_system.repository.RefreshTokenRepo;
import com.user_management_system.repository.UserRepo;
import com.user_management_system.security.JwtService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
@NoArgsConstructor
public class UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private RefreshTokenRepo tokenRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	/**
	 * 
	 * @param authRequest
	 * @return {@link AuthResponse}
	 *         <p>
	 *         The method checks whether the user is authenticated or not
	 * 
	 *         <p>
	 *         {@code Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUserEmail(), authRequest.getUserPassword()));}
	 * 
	 *         <p>
	 *         If authenticated returns the token as AuthResponse to the use, If not
	 *         authenticated throws {@link UserNameNotFoundException}
	 */
	public ResponseEntity<AuthResponse> getAuthenticatedToken(AuthRequest authRequest) {
		log.info("Authenticating request...");
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUserEmail(), authRequest.getUserPassword()));
		if (authentication.isAuthenticated()) {
			String token = jwtService.generateToken(authRequest.getUserEmail());
			RefreshToken refreshToken = jwtService.GenerateRefreshToken();
			User user = userRepo.findByUserEmail(authentication.getName());
			validateAndAddRefreshTokenToUser(refreshToken, user);
			log.info("User updated with new Refersh and Access Token!");
			return new ResponseEntity<AuthResponse>(new AuthResponse(token, refreshToken.getRefreshToken()),
					HttpStatus.OK);
		} else
			throw new UserNameNotFoundException("Failed to Authenticate the user!!");

	}

	private void validateAndAddRefreshTokenToUser(RefreshToken refreshToken, User user) {
		log.info("Updating user Refersh Token...");
		RefreshToken exRefreshToken = user.getRefreshToken();
		// add new token to user if token is null, if present update.
		if (exRefreshToken == null) {
			user.setRefreshToken(refreshToken);
			refreshToken = tokenRepo.save(refreshToken);
			userRepo.save(user);
		} else {
			refreshToken.setTokenId(exRefreshToken.getTokenId());
			refreshToken = tokenRepo.save(refreshToken);
		}
	}

	public ResponseEntity<AuthResponse> refreshToken(RefreshTokenRequest refreshToken) {
		log.info("Requested to refresh the user Access Token...");
		RefreshToken exToken = tokenRepo.findByRefreshToken(refreshToken.getRefreshToken());
		if (exToken != null) {
			if (exToken.getExpiration().compareTo(new Date(System.currentTimeMillis())) >= 0) {
				User user = userRepo.findByRefreshToken(exToken);
				if (user != null) {
					String token = jwtService.generateToken(user.getUserEmail());
					RefreshToken newRefreshToken = jwtService.GenerateRefreshToken();
					validateAndAddRefreshTokenToUser(newRefreshToken, user);
					log.info("User updated with new Refersh and Access Token!");
					return new ResponseEntity<AuthResponse>(new AuthResponse(token, newRefreshToken.getRefreshToken()),
							HttpStatus.OK);
				} else
					throw new UserNotFoundWithRefreshTokenException("Failed to refresh Access Token.");
			} else
				throw new RefreshTokenExpiredException("Failed to refresh Access Token.");
		} else
			throw new InvalidRefreshTokenException("Failed to refresh Access Token.");
	}

	public ResponseEntity<User> saveUser(UserRequest userRequest, String userRole) {
		log.info("New registration request...");
		UserRole role = UserRole.valueOf(userRole.toUpperCase());
		User user = User.builder().userName(userRequest.getUserName()).userEmail(userRequest.getUserEmail())
				.userRole(role).userPassword(passwordEncoder.encode(userRequest.getUserPassword())).build();
		user = userRepo.save(user);
		log.info("User sign-up successful!");
		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}

	public ResponseEntity<List<User>> getUsers() {
		return new ResponseEntity<List<User>>(userRepo.findAll(), HttpStatus.FOUND);
	}

}
