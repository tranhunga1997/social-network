package com.socialnetwork.common.repositories;
import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.socialnetwork.common.entities.LoginTokenInfo;

@Repository
@Transactional
public interface LoginTokenInfoRepository extends JpaRepository<LoginTokenInfo, Long>{
	
	LoginTokenInfo findByRefreshToken(String refreshToken);
	
	LocalDateTime findtokenExpiredDateByRefreshToken(String refreshToken);
}
