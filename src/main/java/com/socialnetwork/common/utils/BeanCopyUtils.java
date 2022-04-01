package com.socialnetwork.common.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
/**
 * sao chép thuộc tính
 * @author thuong
 *
 */
public class BeanCopyUtils {
	/**
	 * Sao chép thuộc tính từ source sang target, loại trừ các giá trị null trong source
	 * @param source
	 * @param target
	 */
	public static void copyProperties(Object source, Object target) {
		copyProperties(source, target, false);
	}
	/**
	 * Sao chép thuộc tính từ source sang target
	 * @param source nguồn
	 * @param target đích
	 * @param copyNull có sao chép thuộc tính null hay không
	 */
	public static void copyProperties(Object source, Object target, boolean copyNull) {
		// Lấy fields của source
		Map<String, Field> sourceFields = getFieldMap(source.getClass(), true, false);
		// Lấy fields của target
		Map<String, Field> targetFields = getFieldMap(target.getClass(), false, false);
		// copy thuộc tính cùng tên
		for(String name : sourceFields.keySet()) {
			Field targetField = targetFields.get(name);
			Field sourceField = sourceFields.get(name);
			if(targetField== null)
				continue;
			try {
				targetField.setAccessible(true);
				sourceField.setAccessible(true);
				Object value = sourceField.get(source);
				// kiểm tra copy null
				if(value != null || copyNull)
					targetField.set(target, value);
			}catch (Exception e) {
				// Nothing
			}
		}
	}
	
	public static Map<String, Field> getFieldMap(Class<?> tempClass, boolean getFinal, boolean getStatic){
		Map<String, Field> sourceFields = new HashMap<String, Field>();
		while(tempClass!=null && !tempClass.equals(Object.class)) {
			for(Field f: tempClass.getDeclaredFields()) {
				// kiểm tra đã tồn tại field hay chưa (xảy ra khi trùng tên field của super)
				if(sourceFields.containsKey(f.getName()))
					continue;
				// các modifier final=16, static=8...
				if(!getFinal && (f.getModifiers()>>4) % 2 ==1)
					continue;
				if(!getStatic && (f.getModifiers()>>3) % 2 ==1)
					continue;
				sourceFields.put(f.getName(), f);
			}
			tempClass = tempClass.getSuperclass();
		}
		return sourceFields;
	}
}
