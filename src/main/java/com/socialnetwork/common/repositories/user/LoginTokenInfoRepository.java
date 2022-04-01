package com.socialnetwork.common.repositories.user;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.socialnetwork.common.entities.user.LoginTokenInfo;

@Repository
public interface LoginTokenInfoRepository extends JpaRepository<LoginTokenInfo, String>{

	LocalDateTime findTokenExpiredAtByRefreshToken(String refreshToken);

}
