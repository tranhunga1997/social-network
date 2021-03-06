package com.socialnetwork.general.user.dtos;

import java.time.LocalDateTime;

import com.socialnetwork.common.entities.user.AuthenticateInfo;
import com.socialnetwork.common.utils.BeanCopyUtils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthenticateInfoDto {
	private Long userId;
	private Integer historyId;
	private String password;
	private Integer loginFailedCounter;
	private LocalDateTime createAt;
	private LocalDateTime updateAt;
	private LocalDateTime deleteAt;
	
	public AuthenticateInfoDto(AuthenticateInfo authenInfo) {
		BeanCopyUtils.copyProperties(authenInfo, this);
	}
	
	public AuthenticateInfo toAuthenticateInfo() {
		AuthenticateInfo authenInfo = new AuthenticateInfo();
		BeanCopyUtils.copyProperties(this, authenInfo);
		return authenInfo;
	}
}
