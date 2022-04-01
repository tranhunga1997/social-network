package com.socialnetwork.user.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socialnetwork.common.entities.user.AuthenticateInfo;
import com.socialnetwork.common.exceptions.SocialException;
import com.socialnetwork.common.repositories.user.AuthenticateRepository;
import com.socialnetwork.common.utils.StringUtil;
import com.socialnetwork.user.dtos.AuthenticateInfoDto;

@Service
public class AuthenticateService {
	@Autowired
	private AuthenticateRepository authenticateRepository;
	@Autowired
	private UserService userService;
	
	// tìm thông tin mật khẩu (new)
	/**
	 * Tìm thông tin mật khẩu (new)
	 * @param userId
	 * @return thông tin mật khẩu (new)
	 */
	public AuthenticateInfoDto findNewInfo(long userId) {
		AuthenticateInfo authenticateInfo = authenticateRepository.findNewInfo(userId);
		AuthenticateInfoDto dto = new AuthenticateInfoDto(authenticateInfo);
		return dto;
	}
	// tìm thông tin mật khẩu (old)
	/**
	 * Tìm thông tin mật khẩu (old)
	 * @param userId
	 * @return danh sách thông tin mật khẩu (cũ)
	 */
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
	/**
	 * Tạo thông tin mật khẩu
	 * @param dto
	 * @return thông tin mật khẩu đã tạo
	 * @throws SocialException
	 */
	public AuthenticateInfoDto create(AuthenticateInfoDto dto) throws SocialException {
		AuthenticateInfo authenInfo = null;
		dto.setHistoryId(1);
		authenInfo = dto.toAuthenticateInfo();
		AuthenticateInfo result = authenticateRepository.save(authenInfo);
		return new AuthenticateInfoDto(result);
	}
	
	// thay đổi mật khẩu
	/**
	 * Thay đổi mật khẩu
	 * @param userId
	 * @param password
	 * @throws SocialException
	 */
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
		authInfo.setDeleteAt(LocalDateTime.now());
		authenticateRepository.save(authInfo);
		
		// tạo thông tin (history+1)
		authInfo.setHistoryId(authInfo.getHistoryId()+1);
		authInfo.setPassword(password);
		authInfo.setCreateAt(LocalDateTime.now());
		authInfo.setDeleteAt(null);
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
