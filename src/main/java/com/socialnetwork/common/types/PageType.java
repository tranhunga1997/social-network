package com.socialnetwork.common.types;

import com.socialnetwork.common.exceptions.SocialException;
/**
 * 
 * @author thuong
 *
 */
public enum PageType {
	UNIT('0'),//trang cá nhân
	PRIVATE('1'),// trang kín
	PUBLIC('2'); // trang công khai
	private char value;
	PageType(char value) {
		this.value = value;
	}
	public char getValue() {
		return value;
	}
	
	public static PageType from(Character dbValue) throws SocialException {
		if(dbValue == null)
			return null;
		switch(dbValue) {
		case '0':
			return UNIT;
		case '1':
			return PRIVATE;
		case '2':
			return PUBLIC;
		default:
			throw new SocialException("W_00001");
		}
	}
}
