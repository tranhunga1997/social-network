package com.socialnetwork.common.repositories;

import org.springframework.data.repository.CrudRepository;

import com.socialnetwork.common.entities.PageMemberInfo;
import com.socialnetwork.common.entities.PageMemberPk;

public interface PageMemberInfoRepository extends CrudRepository<PageMemberInfo, PageMemberPk>{

}
