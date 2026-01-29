package com.electronic.store;

import com.electronic.store.entities.User;
import com.electronic.store.repositories.UserRepository;
import com.electronic.store.security.JwtHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ElectronicStoreApplicationTests {

	@Test
	void contextLoads() {
	}
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JwtHelper jwtHelper;
	@Test
	void testToken(){
		User user = userRepository.findByEmail("admin123@gmail.com").get();
		String token = jwtHelper.generateToken(user);
		System.out.println("Token--"+token);
		System.out.println("expiry -- "+jwtHelper.expirationDateOfToken(token));
		System.out.println("all claims -- "+jwtHelper.getAllClaimsFromToken(token));
		System.out.println("username-- "+jwtHelper.getUsernameFromToken(token));
		System.out.println("is it expired -- "+jwtHelper.isTokenExpired(token));

	}

}
