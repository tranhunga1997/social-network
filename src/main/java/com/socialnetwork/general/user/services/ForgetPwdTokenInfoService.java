package com.socialnetwork.general.user.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Objects;
import com.socialnetwork.common.entities.user.ForgetPwdTokenInfo;
import com.socialnetwork.common.exceptions.SocialException;
import com.socialnetwork.common.repositories.user.ForgetPwdTokenRepository;
import com.socialnetwork.common.utils.StringUtil;
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
		// lấy tất cả token
		String token = TokenProvider.generateTokenNumber();
		int idx = 0;
		List<ForgetPwdTokenInfoDto> datas = findAll();
		String[] tokens = datas.stream().map(data -> data.getToken()).toArray(String[]::new);
				
		// kiểm tra token tránh bị trùng	
		while(true) {
			if(0 == tokens.length || idx >= tokens.length) {
				break;
			} else if(Objects.equal(tokens[idx], token)) {
				token = TokenProvider.generateTokenNumber();
				idx = 0;
			} else {				
				idx++;
			}
		}
		
		if(StringUtil.isNull(token)) {
			throw new SocialException("W_00020", "Token");
		} else {
			entity.setToken(token);
		}
		entity.setUserId(userId);
		entity.setTokenExpiredAt(LocalDateTime.now().plusSeconds(EXPYRIED_DATETIME));
		entity.setCreateAt(LocalDateTime.now());
		return new ForgetPwdTokenInfoDto(forgetPwdTokenRepository.save(entity));
	}
	
	/**
	 * Tìm kiếm tất cả
	 * @return danh sách token
	 */
	public List<ForgetPwdTokenInfoDto> findAll() {
		List<ForgetPwdTokenInfo> entities = forgetPwdTokenRepository.findAll();
		List<ForgetPwdTokenInfoDto> dtos = new ArrayList<>();
		entities.forEach(e -> dtos.add(new ForgetPwdTokenInfoDto(e)));
		return dtos;
	}
	
	/**
	 * Tìm kiếm token
	 * @param tokenId
	 * @return
	 */
	public ForgetPwdTokenInfoDto find(long tokenId) {
		ForgetPwdTokenInfo entity = forgetPwdTokenRepository.findById(tokenId).get();
		ForgetPwdTokenInfoDto dto = new ForgetPwdTokenInfoDto(entity);
		return dto;
	}
	
	/**
	 * Tìm kiếm token
	 * @param token
	 * @return
	 */
	public ForgetPwdTokenInfoDto find(String token) {
		ForgetPwdTokenInfo entity = forgetPwdTokenRepository.findByToken(token);
		ForgetPwdTokenInfoDto dto = null;
		if(StringUtil.isNull(entity)) {
			throw new SocialException("W_00011", "thay đổi mật khẩu");
		}
		dto = new ForgetPwdTokenInfoDto(entity);			
		return dto;
	}
	
	/**
	 * Kiểm tra token tồn tại
	 * @param tokenId
	 * @return
	 */
	public boolean isExists(long tokenId) {
		return forgetPwdTokenRepository.existsById(tokenId);
	}
	
	/**
	 * Kiểm tra token tồn tại
	 * @param token
	 * @return
	 */
	public boolean isExists(String token) {
		return forgetPwdTokenRepository.existsByToken(token);
	}
	
	/**
	 * Xóa token
	 * @param token
	 */
	public void delete(String token) {
		forgetPwdTokenRepository.deleteByToken(token);
	}
}
