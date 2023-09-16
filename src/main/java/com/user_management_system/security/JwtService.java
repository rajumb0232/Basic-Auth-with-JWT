package com.user_management_system.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

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

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getLoginKey()).build().parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
			final String username = extractUsername(token);
			return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	/*
	 * Methods to Generate new Token
	 */
	public String generateToken(String userEmail) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userEmail);
	}

	private String createToken(Map<String, Object> claims, String userEmail) {
		return Jwts.builder().setClaims(claims).setSubject(userEmail).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
				.signWith(getLoginKey(), SignatureAlgorithm.HS256).compact();
	}

	private Key getLoginKey() {
		System.out.println(signInkey);
		byte[] keyBytes = Decoders.BASE64.decode(signInkey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

}
