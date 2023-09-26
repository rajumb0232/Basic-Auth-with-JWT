package com.user_management_system.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user_management_system.util.ErrorStructure;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.info("Authenticating the user...");
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String userName = null;
		try {
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				token = authHeader.substring(7);
					userName = jwtService.extractUsername(token);
			}
			if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
				if (jwtService.validateToken(token, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userName,null,
							userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
					log.info("User successfully authenticated!");
				}
			}
		} catch (ExpiredJwtException e) {
			handleJwtTokenExpiredException(response, e);
		} catch(JwtException e) {
			handleOtherJwtExceptions(response, e);
		}
		filterChain.doFilter(request, response);
	}
	
	
	private void handleOtherJwtExceptions(HttpServletResponse response, JwtException ex) throws StreamWriteException, DatabindException, IOException {
		log.error("ERROR Logging in : "+ ex.getMessage());
		response.setHeader("error", ex.getMessage());
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType("Application/json");
		ErrorStructure error = new ErrorStructure();
		error.setStatus(HttpStatus.FORBIDDEN.value());
		error.setMessage("Failed to Authenticate.");
		error.setRootCause(ex.getMessage());
		new ObjectMapper().writeValue(response.getOutputStream(), error);
	}


	private void handleJwtTokenExpiredException(HttpServletResponse response, ExpiredJwtException ex) throws StreamWriteException, DatabindException, IOException {
		log.error("ERROR Logging in : "+ ex.getMessage());
		response.setHeader("error", ex.getMessage());
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType("Application/json");
		ErrorStructure error = new ErrorStructure();
		error.setStatus(HttpStatus.FORBIDDEN.value());
		error.setMessage("Access Token Expired.");
		error.setRootCause(ex.getMessage());
		new ObjectMapper().writeValue(response.getOutputStream(), error);
	}
}
