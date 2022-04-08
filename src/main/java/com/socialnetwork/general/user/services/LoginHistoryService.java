package com.socialnetwork.general.user.services;

import org.springframework.stereotype.Service;

import com.socialnetwork.common.entities.user.LoginHistoryInfo;
import com.socialnetwork.common.repositories.user.LoginHistoryRepository;
import com.socialnetwork.general.user.dtos.LoginHistoryInfoDto;

@Service
public class LoginHistoryService {
	private LoginHistoryRepository loginHistoryRepository;

	public LoginHistoryInfoDto create (LoginHistoryInfoDto dto) {
		LoginHistoryInfo entity = dto.toLoginHistoryInfo();
		loginHistoryRepository.save(entity);
		return dto;
	}
}
