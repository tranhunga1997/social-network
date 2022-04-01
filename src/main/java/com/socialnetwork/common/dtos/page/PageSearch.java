package com.socialnetwork.common.dtos.page;

import com.socialnetwork.common.dtos.AbstractSearch;
import com.socialnetwork.common.types.PageType;
import com.vvt.search.v2.column.SearchColumn;
import com.vvt.search.v2.type.CompareType;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
public class PageSearch extends AbstractSearch{
	@SearchColumn(priority = 10)
	private Long pageId;
	@SearchColumn(priority = 8)
	private Long ownerId;
	@SearchColumn(name="pageName",compare = CompareType.HasContains, priority = 9)
	private String pageName;
	@SearchColumn(priority = 7)
	private PageType pageType;
	@SearchColumn
	private boolean delFlag;
	@SearchColumn
	private boolean blockFlag;
}
