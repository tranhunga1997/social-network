package com.socialnetwork.common.repositories.user;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.socialnetwork.common.entities.user.AuthenticateInfo;
import com.socialnetwork.common.entities.user.AuthenticatePk;

@Repository
public interface AuthenticateRepository extends JpaRepository<AuthenticateInfo, AuthenticatePk>{
	AuthenticateInfo findByUserId(long userId);
	
	
	@Query(value = "SELECT authen.* FROM m_authenticate_info authen WHERE "
			+ "authen.history_id = (SELECT max(a.history_id) FROM m_authenticate_info a WHERE authen.user_id = ?1)",
			nativeQuery = true)
	AuthenticateInfo findNewInfo(long userId);
	
	@Query(value = "SELECT authen.* FROM m_authenticate_info authen WHERE "
			+ "authen.history_id != (SELECT max(a.history_id) FROM m_authenticate_info a WHERE authen.user_id = ?1)",
			nativeQuery = true)
	List<AuthenticateInfo> findOldInfo(long userId);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE #{#entityName} auth SET auth.loginFailedCounter = :counter WHERE auth.id = :id")
	void changeLoginFailed(int counter, long id);
}
