package com.socialnetwork.general.user.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.socialnetwork.common.entities.user.RegistTokenInfo;
import com.socialnetwork.common.utils.BeanCopyUtils;

import lombok.Data;

@Data
public class RegistTokenInfoDto {
	private Long userId;
	private String token;
	private LocalDate tokenExpiredDate;
	private LocalDateTime createDatetime;
	private LocalDateTime updateDatetime;
	private LocalDateTime deleteDatetime;
	
	public RegistTokenInfoDto(RegistTokenInfo entity) {
		BeanCopyUtils.copyProperties(entity, this);
	}
	
	public RegistTokenInfo toRegistTokenInfo() {
		RegistTokenInfo entity = new RegistTokenInfo();
		BeanCopyUtils.copyProperties(this, entity);
		return entity;
	}
}