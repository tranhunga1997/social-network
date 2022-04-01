package com.socialnetwork.common.entities.status;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.socialnetwork.common.entities.user.UserInfo;

import lombok.Data;

@Entity
@Table(name="m_status_content_like_info")
@Data
@IdClass(StatusContentLikePk.class)
public class StatusContentLikeInfo {
	//PAGE_ID
	@Id
	@Column(name="page_id")
	private Long pageId;
	// status_id
	@Id
	@Column(name = "status_id")
	private Long statusId;
	//content_id
	@Id
	@Column(name="content_id")
	private Integer contentId;
	//USER_ID
	@Id
	@Column(name="user_id")
	private Long userId;
	// CREATE_AT
	@Column(name="create_at")
	private LocalDateTime createAt;
	
	// relationship
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="page_id", insertable = false, updatable = false),
		@JoinColumn(name="status_id", insertable = false, updatable = false),
		@JoinColumn(name="content_id", insertable = false, updatable = false)
	})
	private StatusContentInfo statusContentInfo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id", insertable = false, updatable = false)
	private UserInfo userInfo;
}
