package com.socialnetwork.general.user.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.socialnetwork.common.utils.TokenProvider;
import com.socialnetwork.general.user.services.impl.ForgetPwdTokenInfoService;

@SpringBootTest
class ForgetPwdTokenInfoServiceTest {

	@Autowired
	ForgetPwdTokenInfoService service;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	/**
	 * Kiểm tra tạo token
	 * Case: giá trị khởi tạo token trùng với token đã có trong DB
	 * Cách thức test: debug để test cách hoạt động
	 */
	@Test
	void testCreate() {
		try(MockedStatic<TokenProvider> tokenProviderMock = Mockito.mockStatic(TokenProvider.class)) {
			tokenProviderMock.when(TokenProvider::generateTokenNumber).thenReturn("215757");
		}
		service.create(11);
		
	}

	@Test
	void testFindAll() {
		fail("Not yet implemented");
	}

	@Test
	void testFindLong() {
		fail("Not yet implemented");
	}

	@Test
	void testFindString() {
		fail("Not yet implemented");
	}

	@Test
	void testIsExistsLong() {
		fail("Not yet implemented");
	}

	@Test
	void testIsExistsString() {
		fail("Not yet implemented");
	}

	@Test
	void testDelete() {
		fail("Not yet implemented");
	}

}
