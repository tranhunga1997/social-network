package com.socialnetwork.common.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.socialnetwork.common.entities.user.UserInfo;


class TokenProviderTest {

	static String JWT;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		UserInfo userInfo = new UserInfo();
		userInfo.setUsername("admin");
		JWT = TokenProvider.generateJwt(userInfo.getUsername(), 1);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testGenerateToken() {
		String token = TokenProvider.generateToken();
		System.out.println(token.length());
	}

	@Test
	void testGetUserUsernameFromJwt() {
		String username = TokenProvider.getUserUsernameFromJwt(JWT);
		assertEquals("sunico1997", username);
	}
	
	@Test
	void testGetLoginIdFromJwt() {
		String username = TokenProvider.getUserUsernameFromJwt(JWT);
		assertEquals("admin", username);
	}

	@Test
	void testValidateJwt() {
		boolean check = TokenProvider.validateJwt(JWT);
		assertTrue(check);
	}

	@Test
	void testIsJwtExpired() {
		boolean isExpiry = TokenProvider.isJwtExpired(JWT);
		assertTrue(isExpiry);
	}

	@Test
	void testGetExpiryDate() {
		Date expiryDate = TokenProvider.getExpiryDate(JWT);
		System.out.println(expiryDate.toString());
	}

}
