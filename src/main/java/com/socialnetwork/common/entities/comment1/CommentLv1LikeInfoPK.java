package com.socialnetwork.common.entities.comment1;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * @author thuong
 *
 */
@Data
public class CommentLv1LikeInfoPK implements Serializable{
	/**
	 * version 1
	 */
	private static final long serialVersionUID = 1L;
	//PAGE_ID
	private Long pageId;
	
	private Long statusId;
	
	private Long contentId;
	
	private Long commentLv1Id;
	//USER_ID
	private Long userId;
}
