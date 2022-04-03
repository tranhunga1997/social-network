package com.socialnetwork.user.services;

import org.springframework.stereotype.Service;

import com.socialnetwork.common.entities.user.RegistTokenHistory;
import com.socialnetwork.common.repositories.user.RegistTokenHistoryRepository;
import com.socialnetwork.user.dtos.RegistTokenHistoryDto;

@Service
public class RegistTokenHistoryService {
	private RegistTokenHistoryRepository historyRepository;
	
	/**
	 * Thêm token vào bảng lịch sử
	 * @param dto
	 * @return RegistTokenHistoryDto
	 */
	public RegistTokenHistoryDto create (RegistTokenHistoryDto dto) {
		RegistTokenHistory entity = historyRepository.save(dto.toRegistTokenInfo());
		return new RegistTokenHistoryDto(entity);
	}
}
