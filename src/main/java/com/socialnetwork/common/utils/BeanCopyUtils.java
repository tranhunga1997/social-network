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
	public static void copyProperties(Object source, Object target, boolean copyNull) {
		// Lấy fields của source
		Map<String, Field> sourceFields = new HashMap<String, Field>();
		Class<?> tempClass = source.getClass();
		while(tempClass!=null && !tempClass.equals(Object.class)) {
			for(Field f: tempClass.getDeclaredFields()) {
				// kiểm tra đã tồn tại field hay chưa (xảy ra khi trùng tên field của super)
				if(sourceFields.containsKey(f.getName()))
					continue;
				// loại bỏ các modifier final, static...
				if(f.getModifiers()>=4)
					continue;
				sourceFields.put(f.getName(), f);
			}
		}
		// Lấy fields của target
		Map<String, Field> targetFields = new HashMap<String, Field>();
		tempClass = target.getClass();
		while(tempClass!=null && !tempClass.equals(Object.class)) {
			for(Field f: tempClass.getDeclaredFields()) {
				// kiểm tra đã tồn tại field hay chưa (xảy ra khi trùng tên field của super)
				if(targetFields.containsKey(f.getName()))
					continue;
				// loại bỏ các modifier final, static...
				if(f.getModifiers()>=4)
					continue;
				targetFields.put(f.getName(), f);
			}
		}
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
				if(value != null || copyNull)
					targetField.set(target, value);
			}catch (Exception e) {
				// Nothing
			}
			
		}
	}
}
