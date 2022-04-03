package com.socialnetwork.general.user.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socialnetwork.common.entities.user.ForgetPwdTokenInfo;
import com.socialnetwork.common.repositories.user.ForgetPwdTokenRepository;
import com.socialnetwork.common.utils.TokenProvider;
import com.socialnetwork.general.user.dtos.ForgetPwdTokenInfoDto;

@Service
public class ForgetPwdTokenInfoService {
	@Autowired
	private ForgetPwdTokenRepository forgetPwdTokenRepository;
	
	private static final long EXPYRIED_DATETIME = 1*24*60*60; // day * hour * minus * second
	
	/**
	 * thêm và sửa token
	 * @param userId
	 * @return thông tin forgetPwdToken
	 */
	public ForgetPwdTokenInfoDto create(long userId) {
		ForgetPwdTokenInfo entity = new ForgetPwdTokenInfo();
		entity.setToken(TokenProvider.generateToken());
		entity.setTokenExpiredAt(LocalDateTime.now().minusSeconds(EXPYRIED_DATETIME));
		entity.setCreateAt(LocalDateTime.now());
		return new ForgetPwdTokenInfoDto(forgetPwdTokenRepository.save(entity));
	}
	
	/**
	 * Xóa token
	 * @param token
	 */
	public void delete(String token) {
		forgetPwdTokenRepository.deleteByToken(token);
	}
}
