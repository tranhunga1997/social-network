package com.socialnetwork.common.entities.status;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.socialnetwork.common.types.ContentType;

import lombok.Data;

@Table(name="m_status_content_info")
@Entity
@Data
@IdClass(StatusContentPk.class)
public class StatusContentInfo {
	// PAGE_ID
	@Id
	@Column(name = "page_id")
	private Long pageId;
	// STATUS_ID
	@Id
	@Column(name = "status_id")
	private Long statusId;
	//content_id
	@Id
	@Column(name="content_id")
	private Integer contentId;
	//content_type
	@Column(name="content_type")
	@Convert(converter = ContentType.Convert.class)
	private ContentType contentType;
	
	@Column(name="content_text", length = 2000)
	private String contentText;
	
	@Column(name="content_uri", length = 255)
	private String contentUri;
	
	@ManyToOne
	@JoinColumns(value = { 
			@JoinColumn(name = "page_id", insertable = false, updatable = false),
			@JoinColumn(name = "status_id", insertable = false, updatable = false)
	})
	private StatusInfo statusInfo;
}
