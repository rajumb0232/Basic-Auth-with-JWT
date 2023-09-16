package com.user_management_system.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.user_management_system.entity.User;
import com.user_management_system.repository.UserRepo;

@Configuration
public class CustomUserDetailsService {

	@Autowired
	private UserRepo repo;
	

	/**
	 * @return {@link UserDetailsService}
	 * 
	 *         <p>
	 *         The method uses findByUserEmail() method of {@link UserRepo} to get
	 *         the User Credentials from the Database.
	 */
	@Bean
	UserDetailsService getUserDetailsService() {
		return userEmail -> {
			User user = repo.findByUserEmail(userEmail);
			if (user != null) {
				return new CustomeUserDetail(user);
			} else {
				throw new UsernameNotFoundException("Failed to find the User!!");
			}
		};
	}
}
