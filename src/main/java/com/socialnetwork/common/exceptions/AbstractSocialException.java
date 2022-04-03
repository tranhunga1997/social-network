package com.socialnetwork.common.exceptions;

import com.socialnetwork.common.utils.MessageUtils;

import lombok.Getter;
@Getter
public abstract class AbstractSocialException extends RuntimeException{
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
	public AbstractSocialException(String messageCode, Object...args) {
		super(MessageUtils.getMessage(messageCode, args));
		this.messageCode = messageCode;
		this.args = args;
	}
}
