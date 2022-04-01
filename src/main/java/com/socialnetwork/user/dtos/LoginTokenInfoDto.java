package com.socialnetwork.user.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.socialnetwork.common.entities.user.LoginTokenInfo;
import com.socialnetwork.common.utils.BeanCopyUtils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginTokenInfoDto {
	private Long id;
	private String ipAddress;
	private Long userId;
	private String refreshToken;
	private LocalDate tokenExpiredDate;
	private LocalDateTime createDatetime;
	private LocalDateTime updateDatetime;
	private LocalDateTime deleteDatetime;
	
	public LoginTokenInfoDto(LoginTokenInfo info) {
		BeanCopyUtils.copyProperties(info, this);
	}
	
	public LoginTokenInfo toLoginTokenInfo() {
		LoginTokenInfo info = new LoginTokenInfo();
		BeanCopyUtils.copyProperties(this, info);
		return info;
	}
}
