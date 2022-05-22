package com.socialnetwork.common.repositories;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;

public interface CustomDao<E>{
	/**
	 * CustomDao.persist<br>
	 * insert record
	 * @param entity
	 */
	void persist(E entity);
	/**
	 * CustomDao.merge<br>
	 * update record
	 * @param entity
	 * @return
	 */
	E merge(E entity);
	/**
	 * CustomDao.deleteList<br>
	 * delete a list entities
	 * @param entities
	 * @return
	 */
	int deleteList(Collection<E> entities);
	/**
	 * CustomDao.deleteList<br>
	 * delete a array entities
	 * @param entities
	 * @return
	 */
	@SuppressWarnings("unchecked")
	int deleteList(E... entities);
	/**
	 * CustomDao.deleteOne<br>
	 * delete a entity
	 * @param entity
	 * @return
	 */
	int deleteOne(E entity);
	
	/**
	 * Chỉ cần cung cấp id vào trong entity
	 * @param entity
	 * @return
	 */
	E selectForUpdate(E entity, Class<E> clazz);
	/**
	 * Lấy số lượng tìm kiếm
	 * @param search
	 * @return
	 */
	long countBySearch(Class<E> entityClass, Object search);
	/**
	 * Lấy dữ liệu tìm kiếm
	 * @param search
	 * @return
	 */
	List<E> findBySearch(Class<E> entityClass, Object search, Pageable pageable);
}
