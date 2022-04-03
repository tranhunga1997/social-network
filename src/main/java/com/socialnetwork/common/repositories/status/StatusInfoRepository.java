package com.socialnetwork.common.repositories.status;

import org.springframework.data.repository.CrudRepository;

import com.socialnetwork.common.entities.status.StatusInfo;
import com.socialnetwork.common.entities.status.StatusInfoPK;

public interface StatusInfoRepository extends CrudRepository<StatusInfo, StatusInfoPK> {

}
