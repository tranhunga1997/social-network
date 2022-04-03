//package com.socialnetwork.user.services;
//
//import static org.assertj.core.api.Assertions.fail;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import com.socialnetwork.common.exceptions.SocialException;
//import com.socialnetwork.user.dtos.AuthenticateInfoDto;
//
//@SpringBootTest
//class AuthenticateServiceTest {
//
//	@Autowired
//	private AuthenticateService auService;
//	
//	@BeforeAll
//	static void setUpBeforeClass() throws Exception {
//	}
//
//	@AfterAll
//	static void tearDownAfterClass() throws Exception {
//	}
//
//	@BeforeEach
//	void setUp() throws Exception {
//	}
//
//	@AfterEach
//	void tearDown() throws Exception {
//	}
//
//	@Test
//	void testFindNewInfo() {
//		AuthenticateInfoDto dto = auService.findNewInfo(2);
//		assertEquals(3, dto.getHistoryId());
////		fail("Not yet implemented");
//	}
//
//	@Test
//	void testFindOldInfo() {
//		List<AuthenticateInfoDto> dtos = auService.findOldInfo(2);
//		assertEquals(1, dtos.get(0).getHistoryId());
//		assertEquals(2, dtos.get(1).getHistoryId());
////		fail("Not yet implemented");
//	}
//	
//	/**
//	 * trường hợp set id
//	 */
//	@Test
//	void testCreate1() {
//		AuthenticateInfoDto dto = new AuthenticateInfoDto();
//		dto.setId(6L);
//		dto.setUserId(1L);
//		dto.setPassword("abc1234");
//		dto.setHistoryId(3);
//		dto.setLoginFailedCounter(0);
//		dto.setCreateDatetime(LocalDateTime.now());
//		try {
//			auService.create(dto);
//		} catch (SocialException e) {
//			assertEquals("W_00002", e.getMessageCode());
//		}
////		fail("Not yet implemented");
//	}
//	
//	/**
//	 * trường hợp không set id
//	 */
//	@Test
//	void testCreate2() {
//		AuthenticateInfoDto dto = new AuthenticateInfoDto();
//		dto.setUserId(1L);
//		dto.setPassword("abc1234");
//		dto.setHistoryId(3);
//		dto.setLoginFailedCounter(0);
//		dto.setCreateDatetime(LocalDateTime.now());
//		try {
//			AuthenticateInfoDto result = auService.create(dto);
//			assertEquals(8L, result.getId());
//		} catch (SocialException e) {
//			fail("lỗi ngoại lệ. thất bại");
//		}
//	}
//	
//	/**
//	 * <pre>
//	 * <b>Trường hợp</b>
//	 * userId: Không tồn tại
//	 * password: null
//	 * </pre>
//	 */
//	@Test
//	void testUpdate1() {
//		try {
//			auService.update(99999L, null);
//		}catch(SocialException e) {
//			assertEquals("W_00002", e.getMessageCode());
//		}
//		
//	}
//	
//	/**
//	 * <pre>
//	 * <b>Trường hợp</b>
//	 * userId: Không tồn tại
//	 * password: abc12345
//	 * </pre>
//	 */
//	@Test
//	void testUpdate5() {
//		try {
//			auService.update(99999L, "abc12345");
//		}catch(SocialException e) {
//			assertEquals("E_00003", e.getMessageCode());
//		}
//		
//	}
//	
//	/**
//	 * <pre>
//	 * <b>Trường hợp</b>
//	 * userId: tồn tại
//	 * password: null
//	 * </pre>
//	 */
//	@Test
//	void testUpdate2() {
//		try {
//			auService.update(2, null);
//		}catch(SocialException e) {
//			assertEquals("W_00002", e.getMessageCode());
//		}
//	}
//
//	/**
//	 * <pre>
//	 * <b>Trường hợp</b>
//	 * userId: tồn tại
//	 * password: trống
//	 * </pre>
//	 */
//	@Test
//	void testUpdate3() {
//		try {
//			auService.update(2, "   ");
//		}catch(SocialException e) {
//			assertEquals("W_00002", e.getMessageCode());
//		}
//	}
//	
//	/**
//	 * <pre>
//	 * <b>Trường hợp</b>
//	 * userId: tồn tại
//	 * password: 123abc
//	 * </pre>
//	 */
//	@Test
//	void testUpdate4(){
//		try {
//			auService.update(2, "123abcaaaaa");
//		}catch(SocialException e) {
//			fail("Lỗi ngoại lệ. thất bại");
//		}
//	}
//	
//	@Test
//	void testGetHistoryId() {
//		// trường hợp đúng
//		try {
//			int result1 = auService.getHistoryId(2);
//			assertEquals(3, result1);
//		}catch(SocialException e) {
//			fail("Thất bại.");
//		}
//		
//		// trường hợp sai
//		try {
//			int result2 = auService.getHistoryId(4);
//		}catch(SocialException e) {
//			assertEquals("E_00003", e.getMessageCode());
//		}
//		
//	}
//
//	@Test
//	void testLoginFailedAction() {
//		auService.loginFailedAction(2, 1);
//	}
//
//}
