package com.vvt.search.v2.column;

import javax.persistence.criteria.JoinType;

import com.vvt.search.v2.type.CompareJoinType;
import com.vvt.search.v2.type.SearchArrayType;

public class SearchJoinColumnInfo {
	private String name;
	private Object value;
	private CompareJoinType compareType;
	private SearchArrayType arrayType;
	private boolean orIsNull;
	private JoinType joinType;
	private int priority;
	/**
	 * 
	 * @return
	 * 		true: search
	 * 		false:skip
	 * @throws com.vvt.search.v2.exception.SearchException: type of value incorrect
	 */
	public boolean check() {
		switch(compareType) {
		case Equal:
		case NotEqual:
			return value!=null;
		default:
			return true;
		}
	}
	
	public SearchJoinColumnInfo(String name, Object value, CompareJoinType compareType, SearchArrayType arrayType,
			boolean orIsNull, JoinType joinType, int priority) {
		super();
		this.name = name;
		this.value = value;
		this.compareType = compareType;
		this.arrayType = arrayType;
		this.orIsNull = orIsNull;
		this.joinType = joinType;
		this.priority = priority;
	}


	public SearchJoinColumnInfo(String name, Object value, SearchJoinColumn search) {
		this(name, value, search.compare(), search.arrayType(), search.orIsNull(), search.joinType(), search.priority());
		if(search.name().length()>0)
			this.name =search.name();
	}
	public String getName() {
		return name;
	}
	public Object getValue() {
		return value;
	}
	public CompareJoinType getCompareType() {
		return compareType;
	}
	public SearchArrayType getArrayType() {
		return arrayType;
	}
	public boolean isOrIsNull() {
		return orIsNull;
	}
	public JoinType getJoinType() {
		return joinType;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	
}
