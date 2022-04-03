package com.socialnetwork.general.user.services;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socialnetwork.common.entities.user.AuthenticateInfo;
import com.socialnetwork.common.exceptions.SocialException;
import com.socialnetwork.common.repositories.user.AuthenticateRepository;
import com.socialnetwork.common.utils.MailService;
import com.socialnetwork.common.utils.StringUtil;
import com.socialnetwork.general.user.dtos.AuthenticateInfoDto;
import com.socialnetwork.general.user.dtos.ForgetPwdTokenInfoDto;
import com.socialnetwork.general.user.dtos.UserInfoDto;

/**
 * Xử lấy các thông tin mật khẩu
 * @author Mạnh Hùng
 *
 */
@Service
public class AuthenticateService {
	@Autowired
	private AuthenticateRepository authenticateRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private ForgetPwdTokenInfoService forgetPwdTokenInfoService;
	@Autowired
	private MailService mailService;
	

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
	
	/**
	 * Lấy history id (hiện tại chưa sử dụng)
	 * @param userId
	 * @return
	 * @throws SocialException
	 */
	@Deprecated
	public int getHistoryId(long userId) throws SocialException{
		AuthenticateInfo authenticateInfo = authenticateRepository.findNewInfo(userId);
		if(StringUtil.isNull(authenticateInfo)) {
			throw new SocialException("E_00003");
		}
		return authenticateInfo.getHistoryId();
	}
	
	// quên mật khẩu - viết khi hoàn thành tính năng token
	public void forgetPassword(String email) {
		// lấy thông tin tài khoản từ email
		UserInfoDto userInfoDto = userService.findByEmail(email);
		if(StringUtil.isNull(userInfoDto)) {
			return;
		}
		
		// tạo token
		ForgetPwdTokenInfoDto forgetPwdTokenInfoDto = forgetPwdTokenInfoService.create(userInfoDto.getUserId());
		// gửi mail
		mailService.sendTextMail(email, "title", "forget password token");
		
	}
	/**
	 * Tăng số lần đăng nhập thất bại
	 * @param authenId
	 * @param counter
	 */
	public void loginFailedAction(long authenId, int counter) {
		authenticateRepository.changeLoginFailed(counter+1, authenId);
	}
}
