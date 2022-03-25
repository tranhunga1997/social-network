package com.socialnetwork.common.utils;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

public class StringUtil {
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
	
	/**
	 * <dd>Giải thích: ngăn không cho tạo instance
	 */
	private StringUtil() {}
	
    /**
     * <dd>Giải thích: chuyển sang chuỗi ký tự dạng slug
     * <dd>Ví dụ: hello world -> hello-world
     * @param input
     * @return String
     */
    public static String toSlug(String input) {
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }
    
    /**
     * <dd>Giải thích: kiểm tra chuỗi null
     * @param objs
     * @return <code>true</code> chuỗi null, <code>false</code> ngược lại
     */
    public static boolean isNull(Object obj) {
    	return obj == null;
    }
    
    /**
     * <dd>Giải thích: kiểm tra ký tự là số tự nhiên
     * @param nums
     * @return <code>true</code> là chuỗi số nguyên, <code>false</code> ngược lại
     */
    public static boolean isNumber(String str) {
    	String pattern = "^\\d+$";
    	if(isNull(str)) {
    		return false;
    	}
    	return Pattern.matches(pattern, str);
    }
	
    /**
     * <dd>Giải thích: kiểm tra chuỗi trống
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
    	str = StringUtils.trimWhitespace(str);
    	return "".equals(str);
    }
}
