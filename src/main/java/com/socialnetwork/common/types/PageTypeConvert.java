package com.socialnetwork.common.types;

import javax.persistence.AttributeConverter;
/**
 * 
 * @author thuong
 *
 */
public class PageTypeConvert implements AttributeConverter<PageType, Character>{

	@Override
	public Character convertToDatabaseColumn(PageType attribute) {
		if(attribute == null )
			return null;
		return attribute.getValue();
	}

	@Override
	public PageType convertToEntityAttribute(Character dbData) {
		if(dbData == null )
			return null;
		return PageType.from(dbData);
	}

}
