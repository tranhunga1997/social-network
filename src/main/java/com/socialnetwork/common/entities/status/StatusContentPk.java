package com.socialnetwork.common.entities.status;

import java.io.Serializable;

import javax.persistence.Column;
import lombok.Data;

@Data
public class StatusContentPk implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// PAGE_ID
	@Column(name = "page_id")
	private Long pageId;
	// STATUS_ID
	@Column(name = "status_id")
	private Long statusId;
	//content_id
	@Column(name="content_id")
	private Integer contentId;
}
