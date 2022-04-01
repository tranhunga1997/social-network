package com.vvt.search.v2.fetch;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.criteria.JoinType;
/**
 * 
 * @author Vu van thuong
 *
 */
public class FetchAttribute {
	public FetchAttribute(String attribute, JoinType joinType) {
		super();
		this.attribute = attribute;
		this.joinType = joinType;
	}
	private String attribute;
	private JoinType joinType;
	private final Set<FetchAttribute> subFetchBuiders = new HashSet<>();
	public String getAttribute() {
		return attribute;
	}
	public Set<FetchAttribute> getSubFetchAttribute() {
		return subFetchBuiders;
	}
	/**
	 * return sub fetch
	 * @param name
	 * @param joinType
	 * @return
	 */
	public FetchAttribute addSubFetch(String name, JoinType joinType) {
		FetchAttribute sub= new FetchAttribute(name, joinType);
		sub.attribute=name;
		subFetchBuiders.add(sub);
		return sub;
	}
	public JoinType getJoinType() {
		return joinType;
	}
	
}
