package com.socialnetwork.page.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socialnetwork.common.repositories.PageFollowInfoRepository;
import com.socialnetwork.common.repositories.PageInfoRepository;
import com.socialnetwork.common.repositories.PageLikeInfoRepository;
import com.socialnetwork.common.repositories.PageMemberInfoRepository;

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
	
	
}
