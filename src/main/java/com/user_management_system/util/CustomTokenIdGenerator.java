package com.user_management_system.util;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.stereotype.Component;

@SuppressWarnings("serial")
@Component
public class CustomTokenIdGenerator implements IdentifierGenerator {

	@Override
	public Object generate(SharedSessionContractImplementor session, Object object) {
		String base = UUID.randomUUID().toString();
		return getRandomTen(base);
	}

	private String getRandomTen(String string) {
		string += new Date(System.currentTimeMillis()).toString();
		StringBuilder customId = new StringBuilder();
		customId.append("RT");
		Random random = new Random();

		for (int i = 0; i < 10; i++) {
			if (string.charAt(i) != ' ') {
				char randomChar = string.charAt(random.nextInt(string.length()));
				customId.append(randomChar);
			} else {
				i--;
				continue;
			}
		}
		return customId.toString();
	}

}
