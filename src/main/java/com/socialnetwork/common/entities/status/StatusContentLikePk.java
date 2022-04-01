package com.socialnetwork.common.entities.status;

import java.io.Serializable;

import lombok.Data;
@Data
public class StatusContentLikePk implements Serializable{
	/**
	 * version 1
	 */
	private static final long serialVersionUID = 1L;
	//PAGE_ID
	private Long pageId;
	
	private Long statusId;
	
	private Long contentId;
	
	//USER_ID
	private Long userId;
}
