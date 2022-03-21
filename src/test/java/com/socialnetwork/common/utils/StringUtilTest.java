package com.socialnetwork.common.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StringUtilTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testToSlug() {
		String str1 = "a a b b";
		String str2 = "abc bca";
		String str3 = "32  241 1234  5   ";
		String str4 = "   []   ";
		
		Assertions.assertEquals("a-a-b-b", StringUtil.toSlug(str1));
		Assertions.assertEquals("abc-bca", StringUtil.toSlug(str2));
		Assertions.assertEquals("32--241-1234--5---", StringUtil.toSlug(str3));
		Assertions.assertEquals("------", StringUtil.toSlug(str4));
		Assertions.assertNotEquals("---[]---", StringUtil.toSlug(str4));
		// fail("Not yet implemented");
	}

	@Test
	void testIsNull() {
		Assertions.assertTrue(StringUtil.isNull(null));
		Assertions.assertFalse(StringUtil.isNull(" "));
		Assertions.assertFalse(StringUtil.isNull(" b "));
//		fail("Not yet implemented");
	}

	@Test
	void testIsNumber() {
		Assertions.assertTrue(StringUtil.isNumber("123098"));
		Assertions.assertFalse(StringUtil.isNumber(null));
		Assertions.assertFalse(StringUtil.isNumber("  "));
		Assertions.assertFalse(StringUtil.isNumber("123a098"));
		Assertions.assertFalse(StringUtil.isNumber("abcsd"));
		
//		fail("Not yet implemented");
	}

	@Test
	void testIsBlank() {
		Assertions.assertTrue(StringUtil.isBlank(""));
		Assertions.assertTrue(StringUtil.isBlank("  "));
		Assertions.assertTrue(StringUtil.isBlank("   "));
		Assertions.assertFalse(StringUtil.isBlank("  a"));
//		fail("Not yet implemented");
	}

}
