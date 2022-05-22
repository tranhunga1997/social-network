package com.socialnetwork.common.dtos.page;

import com.socialnetwork.common.dtos.AbstractSearch;
import com.socialnetwork.common.types.PageType;
import com.vvt.jpa.query.SearchColumn;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
public class PageSearchInput extends AbstractSearch{
	private Long pageId;
	private Long ownerId;
	private String pageName;
	private PageType pageType;
	private boolean delFlag;
	private boolean blockFlag;

}
