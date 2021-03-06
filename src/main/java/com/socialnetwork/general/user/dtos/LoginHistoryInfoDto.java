package com.socialnetwork.general.user.dtos;

import java.time.LocalDateTime;

import com.socialnetwork.common.entities.user.LoginHistoryInfo;
import com.socialnetwork.common.utils.BeanCopyUtils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginHistoryInfoDto {
	private String ipAddress;
	private LocalDateTime accessAt;
	private Long userId;
	
	public LoginHistoryInfoDto(LoginHistoryInfo entity) {
		BeanCopyUtils.copyProperties(entity, this);
	}
	
	public LoginHistoryInfo toLoginHistoryInfo() {
		LoginHistoryInfo entity = new LoginHistoryInfo();
		BeanCopyUtils.copyProperties(this, entity);
		return entity;
	}
}
