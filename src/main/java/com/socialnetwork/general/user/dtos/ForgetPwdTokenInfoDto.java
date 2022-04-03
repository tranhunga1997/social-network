package com.socialnetwork.general.user.dtos;

import java.time.LocalDateTime;

import com.socialnetwork.common.entities.user.ForgetPwdTokenInfo;
import com.socialnetwork.common.utils.BeanCopyUtils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ForgetPwdTokenInfoDto {
	private Long userId;
	private String token;
	private LocalDateTime tokenExpiredAt;
	private LocalDateTime createAt;
	private LocalDateTime updateAt;
	
	public ForgetPwdTokenInfoDto(ForgetPwdTokenInfo entity) {
		BeanCopyUtils.copyProperties(entity, this);
	}
	
	public ForgetPwdTokenInfo toForgetPwdTokenInfo() {
		ForgetPwdTokenInfo entity = new ForgetPwdTokenInfo();
		BeanCopyUtils.copyProperties(this, entity);
		return entity;
	}
}
