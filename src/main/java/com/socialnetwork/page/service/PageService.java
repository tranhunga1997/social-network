package com.socialnetwork.page.service;
/**
 * 
 * @author thuong
 *
 */

import com.socialnetwork.page.dto.PageDto;

public interface PageService {
	PageDto findPageById(Long pageId);
}
