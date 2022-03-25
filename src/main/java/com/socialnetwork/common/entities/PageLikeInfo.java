package com.socialnetwork.common.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.socialnetwork.common.entities.user.UserEntity;

import lombok.Data;

@Entity
@Table(name="m_page_follow_info")
@Data
@IdClass(PageLikePk.class)
public class PageLikeInfo {
	//PAGE_ID
	@Id
	@Column(name="page_id")
	private Long pageId;
	//USER_ID
	@Id
	@Column(name="user_id")
	private Long userId;
	// CREATE_AT
	@Column(name="create_at")
	private LocalDateTime createAt;
	
	//relationship
	
	// relationship
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="page_id", insertable = false, updatable = false)
	private PageInfo pageInfo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id", insertable = false, updatable = false)
	private UserEntity userInfo;
}
