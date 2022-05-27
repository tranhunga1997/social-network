package com.socialnetwork.general.user.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.socialnetwork.general.user.dtos.UserInfoDto;

public interface IUserServiceBase<T1> {
	void create(T1 t);
	
	void update(T1 t);
	
	void deleteById(Object id);
	
}
