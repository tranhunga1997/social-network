package com.socialnetwork.common.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
class MessageUtilsTest {
	@Test
	void testGetMessage(){
		assertEquals("Kiểu trang không xác định", MessageUtils.getMessage("W_00001"));
		assertEquals("Kiểu like không xác định", MessageUtils.getMessage("W_00010"));
		
		
		assertEquals("Lỗi không xác định: ABC", MessageUtils.getMessage("E_90003", "ABC"));
	}

}
