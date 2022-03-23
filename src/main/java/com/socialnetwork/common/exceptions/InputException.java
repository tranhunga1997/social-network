package com.socialnetwork.common.exceptions;

import lombok.Getter;

/**
 * Lỗi xảy ra khi giá trị form bị lỗi
 * Khi gặp lỗi này sẽ trả lại form
 * @author thuong
 *
 */
@Getter
public class InputException  extends RuntimeException{
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
	public InputException(String messageCode, Object...args) {
		this.messageCode = messageCode;
		this.args = args;
	}
}
