package com.socialnetwork.common.repositories.page;

import org.springframework.data.repository.CrudRepository;

import com.socialnetwork.common.entities.page.PageInfo;
import com.socialnetwork.common.repositories.CustomDao;

public interface PageInfoRepository extends CrudRepository<PageInfo, Long>, CustomDao<PageInfo>{
	
}
