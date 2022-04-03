package com.socialnetwork.common.entities.page;

import java.io.Serializable;

import lombok.Data;
/**
 * 
 * @author thuong
 *
 */
@Data
public class PageLikeInfoPK implements Serializable{
	/**
	 * version 1
	 */
	private static final long serialVersionUID = 1L;
	//PAGE_ID
	private Long pageId;
	//USER_ID
	private Long userId;
}
