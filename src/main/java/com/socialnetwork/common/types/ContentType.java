package com.socialnetwork.common.types;

import javax.persistence.AttributeConverter;

import com.socialnetwork.common.exceptions.SocialException;

import lombok.Getter;

public enum ContentType {
	TEXT('1'),
	MEDIA('2'),
	FILE('3');
	@Getter
	private char value;
	ContentType(char c) {
		this.value =c;
	}
	public static ContentType from(Character value) {
		if(value==null)
			return null;
		switch(value) {
		case '1':
			return TEXT;
		case '2':
			return MEDIA;
		case '3':
			return FILE;
		default:
			throw new SocialException("W_00008");
		}
	}
	
	public static class Convert implements AttributeConverter<ContentType, Character>{

		@Override
		public Character convertToDatabaseColumn(ContentType attribute) {
			if(attribute==null)
				return null;
			return attribute.getValue();
		}

		@Override
		public ContentType convertToEntityAttribute(Character dbData) {
			if(dbData==null)
				return null;
			return from(dbData);
		}
		
	}
}
