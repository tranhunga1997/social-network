package com.socialnetwork.general.user.apis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;

import com.socialnetwork.common.exceptions.SocialException;
import com.socialnetwork.general.user.services.UserService;

@SpringBootTest
class UserApiTest {

	private MockMvc mockMvc;
	
	@InjectMocks
	private UserApi userApiMock;
	
	UserService userServiceMock;
	
	@BeforeEach
	void setUp() throws Exception {
		// mock service và instance controller
		userServiceMock = mock(UserService.class);
		userApiMock = new UserApi();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testLogin() {
		String expectedHttpCode = "400";
		String expectedMsg = "Dữ liệu không tồn tại";
		
		
		SocialException thrw = new SocialException("E_00003");
		when(userServiceMock.findByUsername(null)).thenThrow(thrw);
		
		userApiMock.userService = userServiceMock;
		
		this.mockMvc = MockMvcBuilders.standaloneSetup(userApiMock).build();
		try {
			MvcResult result = mockMvc.perform(post("/api/user/login")
					.param("username", "123")
					.param("password", "123")
					)
					.andExpect(status().isBadRequest())
					.andReturn();
			
			String actual = result.getResponse().getContentAsString();
			assertEquals(expectedMsg, actual);
		}catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
