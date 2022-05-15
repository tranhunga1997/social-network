package com.socialnetwork.common.dtos.page;

import java.time.LocalDateTime;

import com.socialnetwork.common.entities.page.PageInfo;
import com.socialnetwork.common.entities.page.VPageSearchInfo;
import com.socialnetwork.common.types.PageType;
import com.socialnetwork.common.utils.BeanCopyUtils;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class PageDto {
	private Long pageId;
	private Long ownerId;
	private String ownerFirstName;
	private String ownerLastName;
	private String pageName;
	private PageType pageType;
	private LocalDateTime createAt;
	private LocalDateTime updateAt;
	private boolean delFlag;
	private LocalDateTime delAt;
	private boolean blockFlag;
	private LocalDateTime blockAt;
	private Long blockCauseId;
	
	private int memberCount;
	private int likeCount;
	private int followCount;
	/**
	 * Lấy giá trị từ entity
	 * @param pageInfo
	 */
	public PageDto(VPageSearchInfo pageInfo) {
		BeanCopyUtils.copyProperties(pageInfo, this);
	}
	public PageDto(PageInfo pageInfo) {
		BeanCopyUtils.copyProperties(pageInfo, this);
	}
	/**
	 * Chuyển 
	 * @return
	 */
	public PageInfo toPageInfo() {
		PageInfo pageInfo = new PageInfo();
		BeanCopyUtils.copyProperties(this, pageInfo);
		return pageInfo;
	}
	
}
