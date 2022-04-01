package com.socialnetwork.common.types;

import javax.persistence.AttributeConverter;

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
	public static class Convert implements AttributeConverter<PageType, Character>{

		@Override
		public Character convertToDatabaseColumn(PageType attribute) {
			if(attribute == null )
				return null;
			return attribute.getValue();
		}

		@Override
		public PageType convertToEntityAttribute(Character dbData) {
			return from(dbData);
		}

	}
}
