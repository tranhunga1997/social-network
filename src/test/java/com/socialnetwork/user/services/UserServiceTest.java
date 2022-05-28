package com.socialnetwork.user.services;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Query;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import com.socialnetwork.common.entities.user.ForgetPwdTokenInfo;
import com.socialnetwork.common.entities.user.PermissionInfo;
import com.socialnetwork.common.entities.user.UserInfo;
import com.socialnetwork.common.repositories.user.ForgetPwdTokenRepository;
import com.socialnetwork.common.repositories.user.PermissionInfoRepository;
import com.socialnetwork.common.repositories.user.UserRepository;
import com.socialnetwork.general.user.dtos.PermissionInfoDto;
import com.socialnetwork.general.user.dtos.UserDetailInfoDto;
import com.socialnetwork.general.user.search.PermissionSearch;
import com.socialnetwork.general.user.services.impl.PermissionInfoService;
import com.socialnetwork.general.user.services.impl.UserService;
import com.vvt.jpa.query.QueryBuilder;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserServiceTest {
	@Mock
	UserRepository userRepository;
//	@InjectMocks
	@Autowired
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
		
		Page<UserDetailInfoDto> userInfoDtoPage = userService.findAll(PageRequest.of(0, 2));
		System.out.println("Total: "+ userInfoDtoPage.getTotalElements());
		System.out.println("Page: "+ userInfoDtoPage.getTotalPages());
		userInfoDtoPage.toSet().forEach(System.out::println);
		
	}

	@Test
	void testFind() {
		Field[] fields = UserInfo.class.getDeclaredFields();
		for(Field field : fields) {
			for(Annotation a : field.getDeclaredAnnotations()) {
				System.out.println(field.getName()+": " + a.annotationType().equals(Id.class));
			}
		}
	}
	
	@Test
	void testCreate() {
		UserDetailInfoDto dto = new UserDetailInfoDto();
		userService.create(dto);
	}
}
