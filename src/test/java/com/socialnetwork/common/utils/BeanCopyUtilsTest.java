package com.socialnetwork.common.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.jupiter.api.Test;

import lombok.Data;
import lombok.NoArgsConstructor;


class BeanCopyUtilsTest {
	
	@Test
	void testCopyPropertiesObjectObject() {
		// không copy các giá trị null, final, static
		Source source = new Source();
		source.setField2(3);
		source.setField3("test3");
		source.setField6(null);
		Target target = new Target();
		BeanCopyUtils.copyProperties(source, target);
		assertEquals("test1", target.getField1());
		assertEquals(0, target.getField22());// ko co field field22 nên ko dc copy
		assertEquals(0, target.getField3());// field3 là khác type nên không copy
		assertEquals("", target.getField4());// final không copy
		assertEquals("", target.field5);// static ko copy
		assertEquals("", target.getField6());// không copy do null
	}

	@Test
	void testCopyPropertiesObjectObjectBoolean() {
		// Kiểm tra với với thuộc tính khác null
		Source source = new Source();
		source.setField2(3);
		source.setField3("test3");
		source.setField6("test6");
		Target target = new Target();
		BeanCopyUtils.copyProperties(source, target, false);
		assertEquals("test1", target.getField1());
		assertEquals(0, target.getField22());// ko co field field22 nên ko dc copy
		assertEquals(0, target.getField3());// field3 là khác type nên không copy
		assertEquals("", target.getField4());// final không copy
		assertEquals("", target.field5);// static ko copy
		assertEquals("test6", target.getField6());
		//kiểm tra khi có field null;
		// copyNull = true
		source = new Source();
		source.setField6(null);
		target = new Target();
		BeanCopyUtils.copyProperties(source, target, true);
		assertEquals(null, target.getField6());
		
		// copyNull = false
		source = new Source();
		source.setField6(null);
		target = new Target();
		BeanCopyUtils.copyProperties(source, target, false);
		assertEquals("", target.getField6());// không copy
	}
	@Test
	void testGetFieldMap() {
		// Lấy field của SuperSource, ko final, không static
		Map<String, Field> fieldMap = BeanCopyUtils.getFieldMap(SuperSource.class, false, false);
		assertEquals(2, fieldMap.size());
		//assertTrue(fieldMap.containsKey("field1"));
		assertTrue(fieldMap.containsKey("field2"));
		assertTrue(fieldMap.containsKey("field3"));
		// Lấy field của SuperSource, có final, không static
		fieldMap = BeanCopyUtils.getFieldMap(SuperSource.class, true, false);
		assertEquals(4, fieldMap.size());
		assertTrue(fieldMap.containsKey("field1"));
		assertTrue(fieldMap.containsKey("field2"));
		assertTrue(fieldMap.containsKey("field3"));
		assertTrue(fieldMap.containsKey("field4"));
		
		// Lấy field của SuperSource, không final, có static
		fieldMap = BeanCopyUtils.getFieldMap(SuperSource.class, false, true);
		assertEquals(3, fieldMap.size());
		assertTrue(fieldMap.containsKey("field2"));
		assertTrue(fieldMap.containsKey("field3"));
		assertTrue(fieldMap.containsKey("field5"));
		// Lấy field của SuperSource, có final, có static
		fieldMap = BeanCopyUtils.getFieldMap(SuperSource.class, true, true);
		assertEquals(5, fieldMap.size());
		assertTrue(fieldMap.containsKey("field1"));
		assertTrue(fieldMap.containsKey("field2"));
		assertTrue(fieldMap.containsKey("field3"));
		assertTrue(fieldMap.containsKey("field4"));
		assertTrue(fieldMap.containsKey("field5"));
		// Lấy thuộc tính class kế thừa
		fieldMap = BeanCopyUtils.getFieldMap(Source.class, true, true);
		assertEquals(6, fieldMap.size());
		assertTrue(fieldMap.containsKey("field1"));
		assertTrue(fieldMap.containsKey("field2"));
		assertTrue(fieldMap.containsKey("field3"));
		assertTrue(fieldMap.containsKey("field4"));
		assertTrue(fieldMap.containsKey("field5"));
		assertTrue(fieldMap.containsKey("field6"));
	}
}


@Data
class SuperSource{
	final String field1 ="test1";
	private int field2; // không có trong target
	private String field3;
	final String field4 = "source";
	public static String field5 = "static";
}
@Data
class Source extends SuperSource{
	private String field6;
	
}
@Data
class SuperTarget{
	private String field1 ="";
	private int field22 = 0;// không có trong source
	private int field3 = 0;// khác type(không thể cast)
	final String field4 = "";
	public static String field5 = "";
}
@Data
class Target extends SuperTarget{
	private String field6 ="";
}