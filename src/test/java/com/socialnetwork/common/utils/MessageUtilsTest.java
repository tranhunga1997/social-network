package com.socialnetwork.common.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
@SpringBootTest
class MessageUtilsTest {
	@Autowired
	ConfigurableApplicationContext context;
	@Test
	void testGetMessage() throws BeansException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		System.out.println("ddd");
		Field field = MessageUtils.class.getDeclaredField("messageSource");
		field.setAccessible(true);
		field.set(null, context.getBean(MessageSource.class));
		assertEquals("Kiểu trang không xác định", MessageUtils.getMessage("W_00001"));
		assertEquals("Kiểu like không xác định", MessageUtils.getMessage("W_00010"));
		
		
		assertEquals("Lỗi không xác định: ABC", MessageUtils.getMessage("E_90003", "ABC"));
	}

}
