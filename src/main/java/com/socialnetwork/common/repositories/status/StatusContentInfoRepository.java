package com.socialnetwork.common.repositories.status;

import org.springframework.data.repository.CrudRepository;

import com.socialnetwork.common.entities.status.StatusContentInfo;
import com.socialnetwork.common.entities.status.StatusContentInfoPK;

public interface StatusContentInfoRepository extends CrudRepository<StatusContentInfo, StatusContentInfoPK>{

}
