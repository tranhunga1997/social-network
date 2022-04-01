package com.socialnetwork.common.entities.page;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.Immutable;

import com.socialnetwork.common.types.BooleanConvert;
import com.socialnetwork.common.types.PageType;

import lombok.Data;

/**
 * Thông tin khi tìm kiếm page
 * @author thuong
 *
 */
@Entity
@Table(name = "v_page_search_info")
@Immutable
@Data
public class VPageSearchInfo {
	//page_id
	@Id
	@Column(name="page_id")
	private Long pageId;
	//owner_id
	@Column(name="owner_id")
	private Long ownerId;
	@Column(name="owner_first_name")
	private String ownerFirstName;
	@Column(name="owner_last_name")
	private String ownerLastName;
	//page_name
	@Column(name="page_name")
	private String pageName;
	//page_type
	@Column(name="page_type")
	@Convert(converter = PageType.Convert.class)
	private PageType pageType;
	//create_at
	@Column(name="create_at")
	private LocalDateTime createAt;
	//update_at
	@Column(name="update_at")
	private LocalDateTime updateAt;
	//del_flag
	@Column(name="del_flag")
	@Convert(converter = BooleanConvert.class)
	private boolean delFlag;
	//del_at
	@Column(name="del_at")
	private LocalDateTime delAt;
	//block_flag
	@Column(name="block_flag")
	@Convert(converter = BooleanConvert.class)
	private boolean blockFlag;
	//block_at
	@Column(name="block_at")
	private LocalDateTime blockAt;
	//block_cause_id
	@Column(name="block_cause_id")
	private Integer blockCauseId;
	//membe_count
	@Column(name="member_count")
	private long memberCount;
	//follow_count
	@Column(name="follow_count")
	private long followCount;
	//like_count
	@Column(name="like_count")
	private long likeCount;
}
