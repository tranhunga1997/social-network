package com.socialnetwork.common.repositories.user;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.socialnetwork.common.entities.user.LoginTokenInfo;

@Repository
public interface LoginTokenInfoRepository extends JpaRepository<LoginTokenInfo, Long>{
	@Query("SELECT e.tokenExpiredAt from LoginTokenInfo e where e.token = :refreshToken ")
	LocalDateTime findTokenExpiredAtByRefreshToken(String refreshToken);

}
