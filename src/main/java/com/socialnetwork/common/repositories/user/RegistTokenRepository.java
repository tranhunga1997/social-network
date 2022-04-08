package com.socialnetwork.common.repositories.user;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.socialnetwork.common.entities.user.RegistTokenInfo;

@Repository
public interface RegistTokenRepository extends JpaRepository<RegistTokenInfo, Long>{
	RegistTokenInfo findByToken(String token);
	
	/**
	 * Sửa token
	 * @param token
	 * @param userId
	 * @return số record được sửa
	 */
	@Transactional
	@Modifying
	@Query(value = "UPDATE T_REGIST_TOKEN_INFO r_token SET r_token.token = ?1, u.update_at = NOW() "
			+ "WHERE r_token.user_id = ?2",
			nativeQuery = true)
	int updateToken(String token, long userId);
}
