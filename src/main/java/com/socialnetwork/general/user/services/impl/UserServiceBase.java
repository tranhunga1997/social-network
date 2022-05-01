package com.socialnetwork.general.user.services.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Id;
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

@Component
public abstract class UserServiceBase<T> implements IUserServiceBase<T> {

	@Autowired
	private EntityManager em;
	
	@Override
	@SuppressWarnings("unchecked")
	public Page<T> findAll(Pageable pageable, Class<T> clazz) {
		String totalDataJPQL = "";
		String dataJPQL = "";
		Query query = null;
		
		QueryBuilder builder = new QueryBuilder(clazz, "e");
		// lấy tổng số data
		totalDataJPQL = builder.selectCount().build();
		query = em.createQuery(totalDataJPQL);
		long totalData = (long) query.getSingleResult();
		
		// lấy data
		dataJPQL = builder.select().build();
		query = em.createQuery(dataJPQL, clazz);
		List<T> dataList = query.setFirstResult((int)pageable.getOffset())
				.setMaxResults(pageable.getPageSize())
				.getResultList();
		
		return new PageImpl<T>(dataList, pageable, totalData);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Page<T> find(Object conditionInstant, Pageable pageable, Class<T> clazz) {
		String totalDataJPQL = "";
		String dataJPQL = "";
		Query query = null;
		
		QueryBuilder builder = new QueryBuilder(clazz, "e");
		// thiết lập điều kiện
		builder = builder.where(conditionInstant);
		
		// lấy tổng số data
		totalDataJPQL = builder.selectCount().build();
		query = em.createQuery(totalDataJPQL);
		setParamQuery(query, builder.getParamMap());
		long totalData = (long) query.getSingleResult();
		
		// lấy data
		dataJPQL = builder.select().build();
		query = em.createQuery(dataJPQL, clazz);
		setParamQuery(query, builder.getParamMap());
		List<T> dataList = query.setFirstResult((int)pageable.getOffset())
				.setMaxResults(pageable.getPageSize())
				.getResultList();
		
		return new PageImpl<T>(dataList, pageable, totalData);
	}

	@Override
	public T create(T t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(T t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Transactional
	public void deleteById(Object id, Class<T> clazz) {
		Field fieldId = findFieldId(clazz);
		
		if(null == fieldId) {
			return;
		}
		
		// khởi tạo criteria delete
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaDelete<T> criteriaDelete = builder.createCriteriaDelete(clazz);
		Root<T> root = criteriaDelete.from(clazz);
		
		// khởi tạo câu truy vấn delete
		criteriaDelete.where(builder.equal(root.get(fieldId.getName()), String.valueOf(id)));
        
		em.createQuery(criteriaDelete).executeUpdate();
	}
	
	/**
	 * Tìm field id của object
	 * @param clazz
	 * @return field id
	 */
	private Field findFieldId(Class<T> clazz) {
		for(Field field : clazz.getDeclaredFields()) {
			for(Annotation a : field.getDeclaredAnnotations()) {
				if(a.annotationType().equals(Id.class)) {
					return field;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Set param điều kiện
	 * @param query
	 * @param map
	 */
	private void setParamQuery(Query query, Map<String, Object> map) {
		map.forEach((key, val) -> {
			query.setParameter(key, val);
		});
	}
	
}
