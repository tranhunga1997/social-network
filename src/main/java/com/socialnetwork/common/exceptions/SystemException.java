package com.socialnetwork.common.exceptions;

import lombok.Getter;

/**
 * Lỗi xảy ra khi gặp trục trặc hệ thống
 * Khi gặp lỗi sẽ thông báo lên quản trị
 * @author thuong
 *
 */
@Getter
public class SystemException  extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Tham số khi in ra thông báo
	private Object[] args;
	// Mã lỗi
	private String messageCode;
	/**
	 * Constructor
	 * @param messageCode
	 * @param args
	 */
	public SystemException(String messageCode, Object...args) {
		this.messageCode = messageCode;
		this.args = args;
	}
}
