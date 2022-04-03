package com.socialnetwork.common.repositories.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.socialnetwork.common.entities.user.LoginHistoryInfo;
import com.socialnetwork.common.entities.user.LoginHistoryPk;
import com.socialnetwork.user.dtos.LoginHistoryInfoDto;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistoryInfo, LoginHistoryPk>{
	
	List<LoginHistoryInfo> findByUserId(long userId);
}
