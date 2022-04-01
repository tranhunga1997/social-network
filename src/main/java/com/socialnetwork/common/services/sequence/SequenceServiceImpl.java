package com.socialnetwork.common.services.sequence;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socialnetwork.common.repositories.sequence.SequenceInfoRepository;
@Service
public class SequenceServiceImpl implements SequenceService{
	@Autowired
	SequenceInfoRepository dao;
	@Override
	@Transactional
	public Long getNextId(String seqName) {
		return dao.nextId(seqName);
	}

}
