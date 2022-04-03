package com.socialnetwork.common.exceptions;

import lombok.Getter;

/**
 * Lỗi xảy ra khi gọi các hàm mà giá trị không hợp lệ.
 * Khi gặp lỗi này sẽ chuyển sang trang Lỗi riêng
 * @author thuong
 *
 */
@Getter
public class SocialException  extends AbstractSocialException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 * @param messageCode
	 * @param args
	 */
	public SocialException(String messageCode, Object... args) {
		super(messageCode, args);
	}
}
