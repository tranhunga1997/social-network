package com.socialnetwork.common.entities.page;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.socialnetwork.common.entities.BaseSocialEntity;
import com.socialnetwork.common.entities.user.UserInfo;
import com.socialnetwork.common.types.PageType;

import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 
 * @author thuong
 */
@Entity
@Table(name="m_page_info")
@Data
@EqualsAndHashCode(callSuper = false)
public class PageInfo extends BaseSocialEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	@Convert(converter = PageType.Convert.class)
	private PageType pageType;

	//relationship
	
	@JoinColumn(name="owner_id", updatable = false, insertable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private UserInfo ownerInfo;
}
