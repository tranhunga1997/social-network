package com.socialnetwork.general.user.services.impl;


import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socialnetwork.common.entities.user.RegistTokenInfo;
import com.socialnetwork.common.exceptions.SocialException;
import com.socialnetwork.common.repositories.user.RegistTokenRepository;
import com.socialnetwork.common.utils.StringUtil;
import com.socialnetwork.common.utils.TokenProvider;
import com.socialnetwork.general.user.dtos.RegistTokenHistoryDto;
import com.socialnetwork.general.user.dtos.RegistTokenInfoDto;

/**
 * Xử lý token đăng ký
 * @author Mạnh Hùng
 *
 */
@Service
public class RegistTokenService {
	@Autowired
	private RegistTokenRepository registTokenRepository;
	@Autowired
	private RegistTokenHistoryService registTokenHistoryService;
	
	/**
	 * Tìm token
	 * @param token
	 * @return RegistTokenInfoDto
	 */
	public RegistTokenInfoDto findByToken(String token) {
		if(StringUtil.isNull(token) || StringUtil.isBlank(token)) {
			throw new SocialException("W_00002");
		}
		
		// Kiểm tra tính tồn tại của dữ liệu
		RegistTokenInfo registTokenInfo = registTokenRepository.findByToken(token);
		if(StringUtil.isNull(registTokenInfo)) {
			throw new SocialException("E_00003");
		}
		
		return new RegistTokenInfoDto(registTokenInfo);
		
	}
	
	/**
	 * Thêm mới token
	 * @param dto
	 * @return RegistTokenInfoDto
	 */
	public RegistTokenInfoDto create (RegistTokenInfoDto dto) {
		if(StringUtil.isNull(dto)) {
			throw new SocialException("W_00002");
		}
		
		RegistTokenInfo entity = dto.toRegistTokenInfo();
		RegistTokenInfoDto result = new RegistTokenInfoDto(registTokenRepository.save(entity));
		return result;
	}
	
	/**
	 * Sửa token
	 * @param userId
	 * @return số record đã sửa
	 */
	public int update(long userId) {
		int result = registTokenRepository.updateToken(TokenProvider.generateToken(), userId);
		return result;
	}
	
	/**
	 * Xóa logic token
	 * @param userId
	 */
	public void deleteLogic(long userId) {
		Optional<RegistTokenInfo> optionalRegistTokenInfo = registTokenRepository.findById(userId);
		if(optionalRegistTokenInfo.isEmpty()) {
			throw new SocialException("E_00003");
		}
		
		// xóa token đã kích hoạt
		RegistTokenInfo registTokenInfo = optionalRegistTokenInfo.get();
		registTokenRepository.delete(registTokenInfo);
		
		// thêm vào bảng lịch sử
		RegistTokenHistoryDto historyDto = new RegistTokenHistoryDto();
		historyDto.setUserId(registTokenInfo.getUserId());
		historyDto.setToken(registTokenInfo.getToken());
		historyDto.setActiveAt(LocalDateTime.now());
		registTokenHistoryService.create(historyDto);
	}
}

