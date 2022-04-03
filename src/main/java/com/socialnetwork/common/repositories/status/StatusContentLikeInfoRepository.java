package com.socialnetwork.common.repositories.status;

import org.springframework.data.repository.CrudRepository;

import com.socialnetwork.common.entities.status.StatusContentLikeInfo;
import com.socialnetwork.common.entities.status.StatusContentLikeInfoPK;

public interface StatusContentLikeInfoRepository extends CrudRepository<StatusContentLikeInfo, StatusContentLikeInfoPK>{
}
