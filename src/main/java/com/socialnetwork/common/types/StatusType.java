package com.socialnetwork.common.types;

import javax.persistence.AttributeConverter;

import com.socialnetwork.common.exceptions.SocialException;

public enum StatusType {
	PRIVATE('0'),//bảo mật
	FRIEND('1'),// Chỉ bạn bè
	PUBLIC('2'); // Công khai
	private char value;
	StatusType(char value) {
		this.value = value;
	}
	public char getValue() {
		return value;
	}
	
	public static StatusType from(Character dbValue) throws SocialException {
		if(dbValue == null)
			return null;
		switch(dbValue) {
		case '0':
			return PRIVATE;
		case '1':
			return FRIEND;
		case '2':
			return PUBLIC;
		default:
			throw new SocialException("W_00007");
		}
	}
	public static class Convert implements AttributeConverter<StatusType, Character>{

		@Override
		public Character convertToDatabaseColumn(StatusType attribute) {
			if(attribute == null)
				return null;
			return attribute.value;
		}

		@Override
		public StatusType convertToEntityAttribute(Character dbData) {
			return from(dbData);
		}
		
	}
}
