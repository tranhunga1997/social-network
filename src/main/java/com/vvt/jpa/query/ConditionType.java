package com.vvt.jpa.query;

public enum ConditionType{
	/**
	 * Equal to "="
	 */
	EQ("="),
	/**
	 * Not equal "<>"
	 */
	NEQ("!="),
	/**
	 * Greater than ">"
	 */
	GT(">"),
	/**
	 * Greater than equal to ">="
	 */
	GE(">="),
	/**
	 * Less than "<"
	 */
	LT("<"),
	/**
	 * less than equal to "<="
	 */
	LE("<="),
	/**
	 * in
	 */
	In("IN"),
	/**
	 * Not in
	 */
	NotIn("NOT IN"),
	/**
	 * like "[value]"
	 */
	Like("LIKE"),
	/**
	 * like"%[value]%"
	 */
	HasContain("LIKE"),
	/**
	 * like"^[value]%"
	 */
	StartWith("LIKE"),
	/**
	 * like"%[value]$"
	 */
	EndWith("LIKE");
	private String value;
	ConditionType(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
	
	
}
