package com.socialnetwork.page.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socialnetwork.common.repositories.page.PageFollowInfoRepository;
import com.socialnetwork.common.repositories.page.PageInfoRepository;
import com.socialnetwork.common.repositories.page.PageLikeInfoRepository;
import com.socialnetwork.common.repositories.page.PageMemberInfoRepository;
import com.socialnetwork.page.dto.PageDto;

/**
 * 
 * @author thuong
 *
 */

@Service
public class PageServiceImpl implements PageService{
	@Autowired
	PageInfoRepository pageInfoRepository;
	@Autowired
	PageFollowInfoRepository pageFollowInfoRepository;
	@Autowired
	PageLikeInfoRepository pageLikeInfoRepository;
	@Autowired
	PageMemberInfoRepository pageMemberInfoRepository;
	@Override
	public PageDto findPageById(Long pageId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
