package com.vvt.jpa.query;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.vvt.jpa.exception.SearchQueryException;

public class SearchColumnInfo {
	private String paramName;
	private String name;
	private Object value;
	private ConditionType conditionType;
	private String groupName;
	private int searchNo;
	public SearchColumnInfo() {
		
	}
	
	public SearchColumnInfo(Field field, Object object) throws SearchQueryException {
		this(field.getName(), getFieldValue(field, object), field.getAnnotation(SearchColumn.class));
	}
	public SearchColumnInfo(Method method, Object object) throws SearchQueryException {
		this(method.getName(), getMethodValue(method, object), method.getAnnotation(SearchColumn.class));
	}
	
	public SearchColumnInfo(String name, Object value, SearchColumn searchColumn) {
		//column name
		this.name = searchColumn.name().length()==0?name:searchColumn.name();
		if(name.contains(" "))
			throw new SearchQueryException("column name has space words");
		//value
		this.value = value;
		if(this.value ==null)
			throw new SearchQueryException(String.format("Skip \"%s\" because value is null", name));
		//condition
		this.conditionType = searchColumn.condition();
		//param name
		this.paramName = searchColumn.paramName().length()==0?name:searchColumn.paramName();
		if(paramName.contains(" "))
			throw new SearchQueryException("param name has space words");
		
		//group name
		if(searchColumn.groupName().length()==0)
			this.groupName = searchColumn.groupName();
		else
			this.groupName ='.' + searchColumn.groupName().toUpperCase();
		this.searchNo = searchColumn.searchNo();
	}
	public void setValue(String value) {
		this.value = value;
	}
	public ConditionType getConditionType() {
		return conditionType;
	}
	public void setConditionType(ConditionType conditionType) {
		this.conditionType = conditionType;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getSearchNo() {
		return searchNo;
	}

	public void setSearchNo(int searchNo) {
		this.searchNo = searchNo;
	}
	
	public static Object getMethodValue(Method method, Object obj) {
		try {
			method.setAccessible(true);
			return method.invoke(obj);
		} catch (Exception e) {
			throw new SearchQueryException("can't access to method \""+ method.getName()+"\"");
		}
	}
	public static Object getFieldValue(Field field, Object obj) {
		try {
			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception e) {
			throw new SearchQueryException("can't access to field \""+ field.getName()+"\"");
		}
	}
}
