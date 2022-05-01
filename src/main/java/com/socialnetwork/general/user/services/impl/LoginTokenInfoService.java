package com.socialnetwork.general.user.services.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socialnetwork.common.entities.user.LoginTokenInfo;
import com.socialnetwork.common.exceptions.SocialException;
import com.socialnetwork.common.repositories.user.LoginTokenInfoRepository;
import com.socialnetwork.common.utils.StringUtil;
import com.socialnetwork.common.utils.TokenProvider;
import com.socialnetwork.general.user.dtos.LoginTokenInfoDto;

@Service
public class LoginTokenInfoService {
	@Autowired
	private LoginTokenInfoRepository loginTokenInfoRepository;
	
	// hạn sử dụng 7 ngày
	public static final long EXPIRY_DAY = 7L;

	/**
	 * Tạo thông tin login token
	 * @param userId
	 * @param ipAddress
	 * @return LoginTokenInfoDto
	 */
	public LoginTokenInfoDto create(long userId, String ipAddress) {
		LoginTokenInfo loginTokenInfo = new LoginTokenInfo();
		loginTokenInfo.setUserId(userId);
		loginTokenInfo.setIpAddress(ipAddress);
		loginTokenInfo.setToken(TokenProvider.generateToken());
		loginTokenInfo.setTokenExpiredAt(LocalDateTime.now().plusDays(EXPIRY_DAY));
		loginTokenInfo.setCreateAt(LocalDateTime.now());
		LoginTokenInfo result = loginTokenInfoRepository.save(loginTokenInfo);
		LoginTokenInfoDto dto = new LoginTokenInfoDto(result);
		return dto;
	}
	
	/**
	 * Kiểm tra refresh token tồn tại
	 * @param id
	 * @return <code>true</code> tồn tại, <code>false</code> không tồn tại
	 */
	public boolean isExists(long id) {
		return loginTokenInfoRepository.existsById(id);
	}
	
	/**
	 * Kiểm tra hiệu lực refresh token
	 * @param refreshToken
	 * @return <code>true</code> còn hiệu lực, <code>false</code> hết hiệu lực
	 */
	public boolean isExpiryDate(String refreshToken) {
		LocalDateTime expiry = loginTokenInfoRepository.findTokenExpiredAtByRefreshToken(refreshToken);
		if(StringUtil.isNull(expiry)) {
			throw new SocialException("E_00003");
		}
		if(LocalDateTime.now().isAfter(expiry)) {
			return false;
		}
		return true;
	}

	/**
	 * Xóa refresh token
	 * @param id
	 */
	public void delete(long id) {
		loginTokenInfoRepository.deleteById(id);
	}
	
	
}
