package com.socialnetwork.general.user.services.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Objects;
import com.socialnetwork.common.exceptions.SocialException;
import com.socialnetwork.general.user.services.IUserServiceBase;
import com.vvt.jpa.query.QueryBuilder;
import com.vvt.jpa.query.SearchQuery;

@Component
public abstract class UserServiceBase<T1> implements IUserServiceBase<T1> {

	@PersistenceContext
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	protected <T2> Page<T2> findAll(Pageable pageable, Class<T2> clazz) {
		SearchQuery<T2> searchQuery = new SearchQuery<T2>(clazz, em);
		searchQuery.pageable(pageable);
		// lấy tổng số data
		long totalData = searchQuery.getCount();
		// lấy data
		List<T2> dataList = searchQuery.getResultList();
		
		return new PageImpl<T2>(dataList, pageable, totalData);
	}

	@SuppressWarnings("unchecked")
	protected <T2> Page<T2> find(Object conditionInstant, Pageable pageable, Class<T2> clazz) {
		SearchQuery<T2> searchQuery = new SearchQuery<T2>(clazz, em);
		searchQuery.pageable(pageable);
		// xét điều kiện tìm kiếm
		searchQuery.where(conditionInstant);
		// lấy tổng số data
		long totalData = searchQuery.getCount();
		// lấy data
		List<T2> dataList = searchQuery.getResultList();
		
		return new PageImpl<T2>(dataList, pageable, totalData);
	}
	
}
