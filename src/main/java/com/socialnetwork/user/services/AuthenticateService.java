package com.socialnetwork.user.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socialnetwork.common.entities.AuthenticateInfo;
import com.socialnetwork.common.entities.UserInfo;
import com.socialnetwork.common.exceptions.SocialException;
import com.socialnetwork.common.repositories.AuthenticateRepository;
import com.socialnetwork.common.repositories.UserRepository;
import com.socialnetwork.common.utils.BeanCopyUtils;
import com.socialnetwork.common.utils.StringUtil;
import com.socialnetwork.user.dtos.AuthenticateInfoDto;

@Service
public class AuthenticateService {
	@Autowired
	private AuthenticateRepository authenticateRepository;
	@Autowired
	private UserService userService;
	
	// tìm thông tin mật khẩu (new)
	public AuthenticateInfoDto findNewInfo(long userId) {
		AuthenticateInfo authenticateInfo = authenticateRepository.findNewInfo(userId);
		AuthenticateInfoDto dto = new AuthenticateInfoDto(authenticateInfo);
		return dto;
	}
	// tìm thông tin mật khẩu (old)
	public List<AuthenticateInfoDto> findOldInfo(long userId) {
		List<AuthenticateInfo> authenticateInfos = authenticateRepository.findOldInfo(userId);
		List<AuthenticateInfoDto> dtos = new ArrayList<>();
		authenticateInfos.forEach(info -> {
			AuthenticateInfoDto dto = new AuthenticateInfoDto(info);
			dtos.add(dto);
		});
		return dtos;
	}
	
	// tạo thông tin mật khẩu
	public AuthenticateInfoDto create(AuthenticateInfoDto dto) throws SocialException {
		if(!StringUtil.isNull(dto.getId())) {
			throw new SocialException("W_00002");
		}
		
		AuthenticateInfo authenInfo = new AuthenticateInfo();
		dto.setHistoryId(1);
		authenInfo = dto.toAuthenticateInfo();
		AuthenticateInfo result = authenticateRepository.save(authenInfo);
		BeanCopyUtils.copyProperties(result, dto);
		return dto;
	}
	
	// thay đổi mật khẩu
	public void update(long userId, String password) throws SocialException{
		// kiểm tra null và blank password
		if(StringUtil.isNull(password) || StringUtil.isBlank(password)) {
			throw new SocialException("W_00002");
		}
		
		AuthenticateInfoDto authenticateInfoDto = findNewInfo(userId);
		
		if(StringUtil.isNull(authenticateInfoDto)) {
			throw new SocialException("E_00003");
		}
		AuthenticateInfo authInfo = authenticateInfoDto.toAuthenticateInfo();
		// xóa thông tin cũ
		authInfo.setDeleteDatetime(LocalDateTime.now());
		authenticateRepository.save(authInfo);
		
		// tạo thông tin (history+1)
		authInfo.setHistoryId(authInfo.getHistoryId()+1);
		authInfo.setPassword(password);
		authInfo.setCreateDatetime(LocalDateTime.now());
		authInfo.setDeleteDatetime(null);
		authenticateRepository.save(authInfo);
	}
	
	// lấy history id
	public int getHistoryId(long userId) throws SocialException{
		AuthenticateInfo authenticateInfo = authenticateRepository.findNewInfo(userId);
		if(StringUtil.isNull(authenticateInfo)) {
			throw new SocialException("E_00003");
		}
		return authenticateInfo.getHistoryId();
	}
	
	// quên mật khẩu - viết khi hoàn thành tính năng token
	
	// tăng số lần đăng nhập thất bại
	public void loginFailedAction(long authenId, int counter) {
		authenticateRepository.changeLoginFailed(counter+1, authenId);
	}
	
	//
}
