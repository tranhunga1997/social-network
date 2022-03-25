package com.socialnetwork.common.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.socialnetwork.common.entities.user.UserEntity;
import com.socialnetwork.common.types.BooleanConvert;

import lombok.Data;

@Entity
@Table(name="m_page_member_info")
@Data
@IdClass(PageMemberPk.class)
public class PageMemberInfo {
	//PAGE_ID
	@Id
	@Column(name="page_id")
	private Long pageId;
	//MEMBER_ID
	@Id
	@Column(name="member_id")
	private Long memberId;
	//HISTORY_ID
	@Column(name="history_id")
	private Integer historyId;
	//PAGE_ROLE_ID
	@Column(name="page_role_id", nullable = false)
	private Long pageRoleId;
	//IS_ACCEPT
	@Column(name="is_accept", nullable = false)
	@Convert(converter = BooleanConvert.class)
	private boolean isAccept;
	//ACCEPT_AT
	@Column(name="accept_at")
	private LocalDateTime acceptAt;
	//CREATE_AT]
	@Column(name="create_at")
	private LocalDateTime createAt;
	//UPDATE_AT
	@Column(name="update_at")
	private LocalDateTime updateAt;
	
	// relationship
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="page_id", insertable = false, updatable = false)
	private PageInfo pageInfo;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="member_id", insertable = false, updatable = false)
	private UserEntity memberInfo;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="page_role_id", insertable = false, updatable = false)
	private PageRoleInfo pageRoleInfo;
}
