package com.socialnetwork.common.entities;

import java.io.Serializable;

import javax.persistence.Column;
import lombok.Data;

@Data
public class PageMemberPk implements Serializable{
	/*
	 * version 1
	 */
	private static final long serialVersionUID = 1L;
	//PAGE_ID
	@Column(name="page_id")
	private Long pageId;
	//MEMBER_ID
	@Column(name="member_id")
	private Long memberId;
}