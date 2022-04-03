package com.socialnetwork.user.dtos;

import java.time.LocalDateTime;

import com.socialnetwork.common.entities.user.RegistTokenHistory;
import com.socialnetwork.common.entities.user.RegistTokenInfo;
import com.socialnetwork.common.utils.BeanCopyUtils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistTokenHistoryDto {
	private Long userId;
	private String token;
	private LocalDateTime activeAt;
	
	public RegistTokenHistoryDto(RegistTokenHistory entity) {
		BeanCopyUtils.copyProperties(this, entity);
	}
	
	public RegistTokenHistory toRegistTokenInfo() {
		RegistTokenHistory entity = new RegistTokenHistory();
		BeanCopyUtils.copyProperties(entity, this);
		return entity;
	}
}
