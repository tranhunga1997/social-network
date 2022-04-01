package com.vvt.search.v2.type;
/**
 * 
 * @author Vu van thuong
 *
 */
public enum CompareType {
	Equal,
	NotEqual,
	Like, // '%%'
	GreaterThan,
	LessThan,
	EqualLessThan,
	EqualGreaterThan,
	HasContains,
	Is
}
