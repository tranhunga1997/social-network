package com.socialnetwork.common.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.socialnetwork.common.entities.user.UserEntity;
import com.socialnetwork.common.types.BooleanConvert;
import com.socialnetwork.common.types.PageType;
import com.socialnetwork.common.types.PageTypeConvert;

import lombok.Data;
/**
 * 
 * @author thuong
 *
 */
@Entity
@Table(name="m_page_info")
@Data
public class PageInfo {
	//page_id
	@Id
	@Column(name="page_id")
	private Long pageId;
	//owner_id
	@Column(name="owner_id", nullable = false)
	private Long ownerId;
	//page_name
	@Column(name="page_name", length = 200, nullable = false)
	private String pageName;
	//page_type
	@Column(name="page_type", nullable = false)
	@Convert(converter = PageTypeConvert.class)
	private PageType pageType;
	//create_at
	@Column(name="create_at")
	private LocalDateTime createAt;
	//update_at
	@Column(name="update_at")
	private LocalDateTime updateAt;
	//del_flag
	@Column(name="del_flag", nullable = false)
	@Convert(converter = BooleanConvert.class)
	private boolean delFlag;
	//del_at
	@Column(name="del_at")
	private LocalDateTime delAt;
	//block_flag
	@Column(name="block_flag", nullable = false)
	@Convert(converter = BooleanConvert.class)
	private boolean blockFlag;
	//block_at
	@Column(name="block_at")
	private LocalDateTime blockAt;
	//block_cause_id
	@Column(name="block_cause_id")
	private Long blockCauseId;

	//relationship
	
	
	@JoinColumn(name="owner_id", updatable = false, insertable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private UserEntity owner;
	
	@JoinColumn(name="block_cause_id", updatable = false, insertable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private BlockCauseInfo blockCauseInfo;
}
