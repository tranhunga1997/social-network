package com.socialnetwork.general.page.service;
/**
 * 
 * @author thuong
 *
 */

import com.socialnetwork.general.page.dto.PageDto;

public interface PageService {
	PageDto findPageById(Long pageId);
}
