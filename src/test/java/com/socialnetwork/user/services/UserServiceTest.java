package com.socialnetwork.user.services;

import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.socialnetwork.common.entities.user.UserInfo;
import com.socialnetwork.common.repositories.user.UserRepository;
import com.socialnetwork.general.user.dtos.UserInfoDto;
import com.socialnetwork.general.user.services.UserService;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserServiceTest {
	@Mock
	UserRepository userRepository;
	@InjectMocks
	UserService userService;
	
	MockMvc mockMvc;
	
	List<UserInfo> userInfosGlobal = new ArrayList<>();
	@BeforeEach
	void setUp() throws Exception {
		// instance mockito
		MockitoAnnotations.initMocks(this);
		
		// data giả
		for(int i =1; i<=10; i++) {
			UserInfo dto = new UserInfo();
			dto.setUsername("user"+i);
			dto.setFirstName("hung"+i);
			dto.setLastName("tran");
			dto.setEnabled(true);
			dto.setCreateAt(LocalDateTime.now());
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
