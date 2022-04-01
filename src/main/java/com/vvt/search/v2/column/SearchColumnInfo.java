package com.vvt.search.v2.column;

import com.vvt.search.v2.exception.SearchException;
import com.vvt.search.v2.type.CompareType;
import com.vvt.search.v2.type.SearchArrayType;

public class SearchColumnInfo {
	private String name;
	private Object value;
	private CompareType compareType;
	private SearchArrayType arrayType;
	private int priority;
	private boolean orIsNull;
	/**
	 * 
	 * @return
	 * 		true: search
	 * 		false:skip
	 * @throws com.vvt.search.v2.exception.SearchException: type of value incorrect
	 */
	public boolean check() {
		switch(compareType) {
		case Is:
			return true;
		case Like:
		case HasContains:
			if(value==null)
				return false;
			if( value instanceof String)
				return true;
			else
				throw new SearchException(String.format("column:%s with compare % must String", this.name,this.compareType));
		default:
			if(value==null)
				return false;
			if( value instanceof Comparable<?>)
				return true;
			else
				throw new SearchException(String.format("column:%s with compare % must is Comparable<?>", this.name,this.compareType));
		}
	}
	public SearchColumnInfo(String name, Object value, CompareType compareType, SearchArrayType arrayType,
			boolean orIsNull, int priority) {
		super();
		this.name = name;
		this.value = value;
		this.compareType = compareType;
		this.arrayType = arrayType;
		this.orIsNull = orIsNull;
		this.priority = priority;
	}
	public SearchColumnInfo(String name, Object value, SearchColumn search) {
		this(name, value, search.compare(), search.arrayType(), search.orIsNull(), search.priority());
		if(search.name().length()>0)
			this.name =search.name();
	}
	public String getName() {
		return name;
	}
	public Object getValue() {
		return value;
	}
	public CompareType getCompareType() {
		return compareType;
	}
	public SearchArrayType getArrayType() {
		return arrayType;
	}
	public boolean isOrIsNull() {
		return orIsNull;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	
}
