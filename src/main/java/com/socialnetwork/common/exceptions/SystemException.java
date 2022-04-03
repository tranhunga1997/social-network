package com.socialnetwork.common.exceptions;

import lombok.Getter;

/**
 * Lỗi xảy ra khi gặp trục trặc hệ thống
 * Khi gặp lỗi sẽ thông báo lên quản trị
 * @author thuong
 *
 */
@Getter
public class SystemException  extends AbstractSocialException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Constructor
	 * @param messageCode
	 * @param args
	 */
	public SystemException(String messageCode, Object...args) {
		super(messageCode, args);
	}
}
