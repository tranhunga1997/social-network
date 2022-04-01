package com.socialnetwork.common.repositories.sequence;

import org.springframework.data.repository.CrudRepository;

import com.socialnetwork.common.entities.sequence.SequenceInfo;
import com.socialnetwork.common.repositories.CustomDao;

public interface TSequenceInfoRepository extends CrudRepository<SequenceInfo, String>, CustomDao<SequenceInfo>{
	
}
