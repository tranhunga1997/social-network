package com.socialnetwork.user.services;

import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.socialnetwork.common.entities.UserInfo;
import com.socialnetwork.common.repositories.UserRepository;
import com.socialnetwork.user.dtos.UserInfoDto;

//@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	@Mock
	UserRepository userRepository;
	@InjectMocks
	UserService userService;
	
	List<UserInfo> userInfosGlobal = new ArrayList<>();
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		for(int i =1; i<=10; i++) {
			UserInfo dto = new UserInfo();
			dto.setUsername("user"+i);
			dto.setFirstName("hung"+i);
			dto.setLastName("tran");
			dto.setEnable(true);
			dto.setCreateDatetime(LocalDateTime.now());
			userInfosGlobal.add(dto);
		}
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testFindAll() {
		Mockito.when(userRepository.findAll()).thenReturn(userInfosGlobal);
		
		List<UserInfoDto> userInfoDtos = userService.findAll();
		userInfoDtos.forEach(System.out::println);
	}

	@Test
	void testFindById() {
		fail("Not yet implemented");
	}

	@Test
	void testFindByUsername() {
		fail("Not yet implemented");
	}

}
