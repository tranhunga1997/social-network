package com.socialnetwork.common.entities.comment2;

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

import com.socialnetwork.common.entities.BaseSocialEntity;
import com.socialnetwork.common.entities.comment1.CommentLv1Info;
import com.socialnetwork.common.entities.comment1.CommentLv1InfoPK;
import com.socialnetwork.common.entities.status.StatusContentInfo;
import com.socialnetwork.common.entities.user.UserInfo;
import com.socialnetwork.common.types.ContentType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "m_comment_lv2_info")

/**
 * 
 * @author thuong
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@IdClass(CommentLv2InfoPK.class)
public class CommentLv2Info extends BaseSocialEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//PAGE_ID
	@Id
	@Column(name = "page_id")
	private Long pageId;
	
	//STATUS_ID
	@Id
	@Column(name = "status_id")
	private Long statusId;
	
	//content_id
	@Id
	@Column(name = "content_id")
	private Integer contentId;
	
	//comment_lv1_id
	@Id
	@Column(name = "comment_lv1_id")
	private Long commentLv1Id;
	
	//comment_lv2_id
	@Id
	@Column(name = "comment_lv2_id")
	private Long commentLv2Id;
	//owner
	@Column(name="owner_id")
	private Long ownerId;
	
	//content_text
	@Column(name="content_text", length = 2000)
	private String contentText;
	
	//content_type
	@Column(name="content_type")
	@Convert(converter = ContentType.Convert.class)
	private ContentType contentType;
	
	//content_uri
	@Column(name="content_uri", length = 255)
	private String contentUri;
	
	
	@JoinColumn(name="owner_id", updatable = false, insertable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private UserInfo ownerInfo;
	
	@JoinColumns({
		@JoinColumn(name="page_id", referencedColumnName = "page_id", insertable = false, updatable = false),
		@JoinColumn(name="status_id", referencedColumnName = "status_id", insertable = false, updatable = false),
		@JoinColumn(name="content_id", referencedColumnName = "content_id", insertable = false, updatable = false),
		@JoinColumn(name="comment_lv1_id", referencedColumnName = "comment_lv1_id", insertable = false, updatable = false)
	})
	@ManyToOne(fetch = FetchType.LAZY)
	private CommentLv1Info commentLv1Info;
	

}
