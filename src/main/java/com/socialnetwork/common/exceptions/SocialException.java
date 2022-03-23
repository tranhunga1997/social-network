package com.socialnetwork.common.exceptions;

import lombok.Getter;

/**
 * Lỗi xảy ra khi gọi các hàm mà giá trị không hợp lệ.
 * Khi gặp lỗi này sẽ chuyển sang trang Lỗi riêng
 * @author thuong
 *
 */
@Getter
public class SocialException  extends RuntimeException{
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
	public SocialException(String messageCode, Object...args) {
		this.messageCode = messageCode;
		this.args = args;
	}
}
