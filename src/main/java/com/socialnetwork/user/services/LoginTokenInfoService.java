package com.socialnetwork.user.services;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socialnetwork.common.entities.LoginTokenInfo;
import com.socialnetwork.common.repositories.LoginTokenInfoRepository;
import com.socialnetwork.common.utils.TokenProvider;
import com.socialnetwork.user.dtos.LoginTokenInfoDto;

@Service
public class LoginTokenInfoService {
	@Autowired
	private LoginTokenInfoRepository loginTokenInfoRepository;
	
	// hạn sử dụng 7 ngày
	public static final long EXPIRY_DAY = 7L;
	
	// tạo thông tin login token
	public LoginTokenInfoDto create(long userId, String ipAddress) {
		LoginTokenInfo loginTokenInfo = new LoginTokenInfo();
		loginTokenInfo.setUserId(userId);
		loginTokenInfo.setIpAddress(ipAddress);
		loginTokenInfo.setToken(TokenProvider.generateToken());
		loginTokenInfo.setTokenExpiredDate(LocalDate.now().plusDays(EXPIRY_DAY));
		loginTokenInfo.setCreateDatetime(LocalDateTime.now());
		LoginTokenInfo result = loginTokenInfoRepository.save(loginTokenInfo);
		LoginTokenInfoDto dto = new LoginTokenInfoDto(result);
		return dto;
	}
	
	// kiểm tra tồn tại
	public boolean isExists(long id) {
		return loginTokenInfoRepository.existsById(id);
	}
}
