package com.socialnetwork.common.utils;

import static org.junit.jupiter.api.Assertions.*;


import javax.mail.MessagingException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MailUtilsTest {
	
	@Autowired
	MailService mailService;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testSendTextMail() {
		mailService.sendTextMail("tranhunga1997@gmail.com", "Test text", "Hi there!!");
//		fail("Not yet implemented");
	}

	@Test
	void testSendHtmlMail() {
		try {
			mailService.sendHtmlMail("tranhunga1997@gmail.com", "Test html", "<h1>Hi there!!</h1>");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		fail("Not yet implemented");
	}

	@Test
	void testReadHtmlTemplateFile() {
//		fail("Not yet implemented");
	}

}
