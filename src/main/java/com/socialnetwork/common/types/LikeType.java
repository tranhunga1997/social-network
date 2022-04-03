package com.socialnetwork.common.types;

import javax.persistence.AttributeConverter;

import com.socialnetwork.common.exceptions.SocialException;

import lombok.Getter;
/**
 * 
 * @author thuong
 *
 */
public enum LikeType {
	LIKE('1'),
	LOVE('2'),
	CRY('3'),
	SMILE('4'),
	LAUGH('5');
	
	@Getter
	private char value;
	
	LikeType(char c) {
		this.value =c;
	}
	
	public static LikeType from(Character dbValue) {
		if(dbValue==null)
			return null;
		switch(dbValue) {
		case '1':
			return LIKE;
		case '2':
			return LOVE;
		case '3':
			return CRY;
		case '4':
			return SMILE;
		case '5':
			return LAUGH;
		default:
			throw new SocialException("W_00010");
		}
	}
	
	public static class Convert implements AttributeConverter<LikeType, Character>{

		@Override
		public Character convertToDatabaseColumn(LikeType attribute) {
			if(attribute ==null)
				return null;
			return attribute.value;
		}

		@Override
		public LikeType convertToEntityAttribute(Character dbData) {
			return from(dbData);
		}
		
	}
}
