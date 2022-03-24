package com.socialnetwork.common.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * sao chép thuộc tính
 * @author thuong
 *
 */
public class BeanCopyUtils {
	/**
	 * Sao chép thuộc tính từ source sang target
	 * @param source
	 * @param target
	 */
	public static void copyProperties(Object source, Object target) {
		for(Field sourceFiled : source.getClass().getDeclaredFields()) {
			try {
				//Lấy thuộc tính cùng tên
				Field targetField = target.getClass().getDeclaredField(sourceFiled.getName());
				//Cho phép truy cập
				sourceFiled.setAccessible(true);
				targetField.setAccessible(true);
				// copy
				targetField.set(target, sourceFiled.get(source));
			}catch (Exception e) {
				//nothing
			}
		}
	}
	/**
	 * chuyển dữ liệu sang dạng map
	 * @param source
	 * @return
	 */
	public static Map<String, Object> getMapFromProperties(Object source, String...propertyNames){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Field> fields; 
		if(propertyNames.length ==0)
			fields = Arrays.asList(source.getClass().getDeclaredFields());
		else {
			fields = new ArrayList<Field>();
			for(String fieldName : propertyNames) {
				try {
					fields.add(source.getClass().getField(fieldName));
				}catch (Exception e) {
					//không tồn tại thuộc tính
					//throw new IllegalArgumentException("property not found");
				}
			}
		}
		
		for(Field field: fields) {
			try {
				field.setAccessible(true);
				map.put(field.getName(), field.get(source));
			}catch(Exception e) {
				// Không lấy được dữ liệu
				//throw new IllegalAccessError("Can't access to property");
			}
		}
		return map;
	}
}
