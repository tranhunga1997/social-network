package com.socialnetwork.common.types;

import javax.persistence.AttributeConverter;

import com.socialnetwork.common.exceptions.SocialException;
/**
 * 
 * @author thuong
 *
 */
public class BooleanConvert implements AttributeConverter<Boolean, Character>{

	@Override
	public Character convertToDatabaseColumn(Boolean attribute) {
		if(attribute== null)
			return null;
		if (attribute)
			return 'T';
		else
			return 'F';
	}

	@Override
	public Boolean convertToEntityAttribute(Character dbData) {
		if(dbData==null)
			return null;
		switch(dbData) {
		case 'T':
			return true;
		case 'F':
			return false;
		default:
			throw new SocialException("W_00002");
		}
	}

}
