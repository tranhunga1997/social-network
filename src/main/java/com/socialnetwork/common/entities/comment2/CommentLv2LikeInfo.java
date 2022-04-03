package com.socialnetwork.common.entities.comment2;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.socialnetwork.common.entities.user.UserInfo;
import com.socialnetwork.common.types.LikeType;

import lombok.Data;

/**
 * 
 * @author thuong
 *
 */
@Entity
@Table(name="m_comment_lv2_like_info")
@Data
@IdClass(CommentLv2LikeInfoPK.class)
public class CommentLv2LikeInfo {
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
	//comment_lv1_id
	@Id
	@Column(name="comment_lv1_id")
	private Long commentLv1Id;
	//comment_lv1_id
	@Id
	@Column(name="comment_lv2_id")
	private Long commentLv2Id;
	//USER_ID
	@Id
	@Column(name="user_id")
	private Long userId;
	//LIKE_TYPE
	@Column(name="like_type")
	@Convert(converter = LikeType.Convert.class)
	private LikeType likeType;
	// CREATE_AT
	@Column(name="create_at")
	private LocalDateTime createAt;
	
	// relationship
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="page_id", referencedColumnName = "page_id", insertable = false, updatable = false),
		@JoinColumn(name="status_id", referencedColumnName = "status_id", insertable = false, updatable = false),
		@JoinColumn(name="content_id", referencedColumnName = "content_id", insertable = false, updatable = false),
		@JoinColumn(name="comment_lv1_id", referencedColumnName = "comment_lv1_id", insertable = false, updatable = false),
		@JoinColumn(name="comment_lv2_id", referencedColumnName = "comment_lv2_id", insertable = false, updatable = false)

	})
	private CommentLv2Info commentLv2Info;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id", insertable = false, updatable = false)
	private UserInfo userInfo;
}
