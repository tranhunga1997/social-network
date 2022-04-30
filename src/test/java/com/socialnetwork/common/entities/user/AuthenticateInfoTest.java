package com.socialnetwork.common.entities.user;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.socialnetwork.general.user.dtos.AuthenticateInfoDto;

class AuthenticateInfoTest {

	AuthenticateInfo entity;
	
	@BeforeEach
	void setUp() throws Exception {
		entity = new AuthenticateInfo();
		entity.setUserId(1L);
		entity.setPassword("123123");
		entity.setHistoryId(1);
		entity.setCreateAt(LocalDateTime.now());
		entity.setUpdateAt(LocalDateTime.now().plusDays(5));
	}

	@Test
	void test() {
		AuthenticateInfoDto dto = new AuthenticateInfoDto(entity);
		assertNotNull(dto.getCreateAt());
		assertNotNull(dto.getUpdateAt());
	}

}
