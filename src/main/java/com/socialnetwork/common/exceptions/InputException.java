package com.socialnetwork.common.exceptions;

import com.socialnetwork.common.utils.MessageUtils;

import lombok.Getter;

/**
 * Lỗi xảy ra khi giá trị form bị lỗi
 * Khi gặp lỗi này sẽ trả lại form
 * @author thuong
 *
 */
@Getter
public class InputException  extends AbstractSocialException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String field;
	/**
	 * Constructor
	 * @param messageCode
	 * @param args
	 */
	public InputException(String field, String messageCode, Object...args) {
		super(messageCode, args);
		this.field = field;
	}
}
