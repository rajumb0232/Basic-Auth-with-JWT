package com.user_management_system.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * The class defines the Security Configurations to the application.
 * 
 * <p>
 * {@code @EnableWebSecurity} enables Spring Security to the Application
 * 
 * <p>
 * {@code @EnableMethodSecurity} enables the class level and method level
 * authorization to the application using {@code @PreAuthorise @PostAuthorize}
 * like Annotations applied over controller class and its methods.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private JwtAuthFilter jwtAuthFilter;

	@Autowired
	private UserDetailsService userDetailsService;

	/**
	 * @return {@link SecurityFilterChain}
	 * 
	 *         <p>
	 *         The method takes {@link HttpSecurity} object as a method parameter It
	 *         defines the CSRF, RequestMatchers (the URLs) that has to be
	 *         authenticated, and authorized and the logoutRequestMatcher.
	 */

	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth.requestMatchers("/**").permitAll().anyRequest().authenticated())
				.formLogin(withDefaults())
				.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST")).permitAll())
				.build();
	}

	@Bean
	SecurityFilterChain securityFilterChainWithJWT(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth.requestMatchers("/**").permitAll().anyRequest().authenticated())
				.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).build();
	}

	/**
	 * @return {@link AuthenticationProvider}
	 *         <p>
	 *         The AuthenticationProvider is an interface responsible for
	 *         authenticating the User Credentials. When there is a request to
	 *         authenticate the user, the spring will get the user credentials from
	 *         the DaoAuthenticationProvider.
	 * 
	 *         <p>
	 *         The DaoAuthenticationProvider takes the UserServiceDetails and
	 *         PasswordEncoder objects and uses them to fetch the User Credentials
	 *         from the database.
	 * 
	 *         <p>
	 *         This is the default mechanism to authenticate the user's in Spring
	 *         Security.
	 * 
	 *         <p>
	 *         The AuthenticationProvider can be configured to perform custom
	 *         authentication by implemented using LDAP or OAuth.
	 */
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(getPasswordEncoder());
		return authProvider;
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfig) throws Exception {
		return authenticationConfig.getAuthenticationManager();
	}

	/**
	 * @return {@link PasswordEncoder}
	 *         <p>
	 *         The PasswordEncoder is an Inbuilt Interface used to encode the
	 *         password, the PassowrdEncoder should be implemented with the desired
	 *         implementation class that is following specific encrypting
	 *         algorithms.
	 * 
	 *         <p>
	 *         ByCrypt is one of the most strong hashing technology available used
	 *         by the class BCryptPasswordEncoder and it implements the
	 *         passwordEncoder Interface.
	 * 
	 *         <p>
	 *         follow links to learn more {@link PasswordEncoder}
	 *         {@link BCryptPasswordEncoder}
	 */
	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
