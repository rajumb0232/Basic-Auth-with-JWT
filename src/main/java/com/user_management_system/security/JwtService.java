package com.user_management_system.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.user_management_system.entity.RefreshToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JwtService {

	@Value("${secret.login.key}")
	private String signInkey;

	/*
	 * Methods to evaluate the Token
	 */
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		log.info("Extracting Claims from JWT.");
		final Claims claims = Jwts.parserBuilder().setSigningKey(getLoginKey()).build().parseClaimsJws(token).getBody();
		return claimsResolver.apply(claims);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		log.info("Validating the token.");
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	/*
	 * Methods to get LoginKey (secret-key)
	 */
	private Key getLoginKey() {
		byte[] keyBytes = Decoders.BASE64.decode(signInkey);
		// HMAC - Hash-Based Message Authentication Code
		// SHA - Secure Hash Algorithms
		return Keys.hmacShaKeyFor(keyBytes);
	}

	/*
	 * Methods to Generate new Token
	 */
	public String generateToken(String userEmail) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userEmail);
	}

	private String createToken(Map<String, Object> claims, String userEmail) {
		log.info("Generating Access Token.");
		return Jwts.builder().setClaims(claims).setSubject(userEmail).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 3))
				.signWith(getLoginKey(), SignatureAlgorithm.HS256).compact();
	}

	/*
	 * Method to Generate Refresh Token
	 */
	public RefreshToken GenerateRefreshToken() {
		log.info("Generating Refresh Token.");
		String token = UUID.randomUUID().toString();

		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setRefreshToken(token);
		refreshToken.setExpiration(new Date(System.currentTimeMillis() + 180L * 24 * 60 * 60 * 1000));
		return refreshToken;
	}

}
