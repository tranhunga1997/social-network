package com.socialnetwork.common.repositories.sequence;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.socialnetwork.common.exceptions.SystemException;
import com.socialnetwork.common.services.sequence.SequenceService;
@SpringBootTest
class SequenceInfoRepositoryTest {
	@Autowired
	SequenceInfoRepository dao;
	@Autowired
	SequenceService service;
	@PersistenceContext
	protected EntityManager em;
	
	@Test
	@Transactional
	void testNextId() {
		em.createQuery("update SequenceInfo s set s.currentNum = :curNum, s.maxNum = :maxNum WHERE s.sequenceId =:seqId")
			.setParameter("seqId", "TEST_ID_SEQ")
			.setParameter("curNum", 5L)
			.setParameter("maxNum", 7L)
			.executeUpdate();
		assertEquals(6, dao.nextId("TEST_ID_SEQ"));
		assertEquals(7, dao.nextId("TEST_ID_SEQ"));
		//current> max
		try {
			assertEquals(8, dao.nextId("TEST_ID_SEQ"));
			fail();
		}catch(SystemException e){
			assertEquals("E90001",e.getMessageCode());
		}
		// không tồn tại id
		try {
			assertEquals(8, dao.nextId("AAA_ID_SEQ"));
			fail();
		}catch(SystemException e){
			assertEquals("E90002",e.getMessageCode());
		}
	}

}
