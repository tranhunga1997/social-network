package com.socialnetwork.general.user.apis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialnetwork.common.dtos.Pagination;
import com.socialnetwork.common.exceptions.SocialException;
import com.socialnetwork.general.user.dtos.RoleDetailInfoDto;
import com.socialnetwork.general.user.services.impl.UserService;

@SpringBootTest
class UserApiTest {
	
	@Autowired
	private WebApplicationContext webApplicationContext;
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

//	@AfterEach
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

	@Test
	void testUpdateRole() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).alwaysDo(log()).build();
		ObjectMapper mapper = new ObjectMapper();
		try {
			MvcResult result = mockMvc.perform(get("/api/user/role")
					.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
					.andExpect(status().is(200))
					.andReturn();
			Pagination<RoleDetailInfoDto> pagination = new Pagination<RoleDetailInfoDto>();
			pagination = mapper.readValue(result.getResponse().getContentAsByteArray(), pagination.getClass());
			for(Object dto : pagination.getDatas()) {
				System.out.println(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
