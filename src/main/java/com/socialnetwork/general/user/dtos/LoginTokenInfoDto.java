package com.socialnetwork.general.user.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.socialnetwork.common.entities.user.LoginTokenInfo;
import com.socialnetwork.common.utils.BeanCopyUtils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginTokenInfoDto {
//	private Long id;
	private String ipAddress;
	private Long userId;
	private String refreshToken;
	private LocalDateTime tokenExpiredAt;
	private LocalDateTime createDatetime;
	private LocalDateTime updateDatetime;
	private LocalDateTime deleteDatetime;
	
	public LoginTokenInfoDto(LoginTokenInfo entity) {
		BeanCopyUtils.copyProperties(entity, this);
		this.refreshToken = entity.getToken();
	}
	
	public LoginTokenInfo toLoginTokenInfo() {
		LoginTokenInfo entity = new LoginTokenInfo();
		BeanCopyUtils.copyProperties(this, entity);
		entity.setToken(this.refreshToken);
		return entity;
	}
}
