package com.socialnetwork.common.entities.status;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.socialnetwork.common.entities.BaseSocialEntity;
import com.socialnetwork.common.entities.page.PageInfo;
import com.socialnetwork.common.entities.user.UserInfo;
import com.socialnetwork.common.types.StatusType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "m_status_info")
@IdClass(StatusPk.class)
@EqualsAndHashCode(callSuper = false)
public class StatusInfo extends BaseSocialEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// PAGE_ID
	@Id
	@Column(name = "page_id")
	private Long pageId;
	// STATUS_ID
	@Id
	@Column(name = "status_id")
	private Long statusId;
	//owner_id
	@Column(name="owner_id", nullable = false)
	private Long ownerId;
	// STATUS_TYPE
	@Column(name = "status_type", nullable = false)
	@Convert(converter = StatusType.Convert.class)
	private StatusType statusType;
	
	
	//relationship
	
	@JoinColumn(name="owner_id", updatable = false, insertable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private UserInfo ownerInfo;
	
	@JoinColumn(name="page_id", updatable = false, insertable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private PageInfo pageInfo;
}
