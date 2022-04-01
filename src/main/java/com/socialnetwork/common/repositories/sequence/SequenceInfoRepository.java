package com.socialnetwork.common.repositories.sequence;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.socialnetwork.common.entities.sequence.SequenceInfo;
import com.socialnetwork.common.exceptions.SystemException;
@Transactional
public interface SequenceInfoRepository extends Repository<SequenceInfo, String> {
	/**
	 * 
	 * @param seqId
	 * @return
	 */
	@Modifying
	@Query("UPDATE SequenceInfo s SET s.currentNum = s.currentNum + 1 WHERE s.sequenceId = :seqId")
	int incSeqenceId(String seqId);
	
	
	@Query("SELECT s.sequenceId, s.currentNum, s.maxNum FROM SequenceInfo s WHERE s.sequenceId = :sequenceId")
	Object[][] getCurrentNum(String sequenceId); 
	
	default Long nextId(String seqId) {
		int updateCount = incSeqenceId(seqId);
		if(updateCount == 0)
			throw new SystemException("E90002", seqId);
		Object[][] records = getCurrentNum(seqId);
		//if(records.length ==0)
		//	return null;
		Object[] record = records[0];
		long current = (long) record[1];
		long max = (long) record[2];
		if(max<current)
			throw new SystemException("E90001", seqId);
		return (Long) record[1];
	}
}
