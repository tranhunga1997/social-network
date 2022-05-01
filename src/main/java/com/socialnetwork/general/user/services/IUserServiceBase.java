package com.socialnetwork.general.user.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserServiceBase<T> {
	
	/**
	 * Tìm tất cả dữ liệu (phân trang)
	 * @param pageable
	 * @param clazz
	 * @return page
	 */
	Page<T> findAll(Pageable pageable, Class<T> clazz);
	
	Page<T> find(Object conditionInstant, Pageable pageable, Class<T> clazz);
	
	T create(T t);
	
	void update(T t);
	
	void deleteById(Object id, Class<T> clazz);
	
}
