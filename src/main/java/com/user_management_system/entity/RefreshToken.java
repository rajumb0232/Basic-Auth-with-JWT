package com.user_management_system.entity;

import java.util.Date;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
	
	@Id
	@GeneratedValue(generator = "custome_id")
	@GenericGenerator(name = "custome_id", type = com.user_management_system.util.CustomTokenIdGenerator.class)
	private String tokenId;
	private String refreshToken;
	private Date expiration;
}
