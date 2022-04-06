package com.socialnetwork.general.user.dtos;

import java.time.LocalDateTime;

import com.socialnetwork.common.entities.user.RegistTokenInfo;
import com.socialnetwork.common.utils.BeanCopyUtils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistTokenInfoDto {
	private Long userId;
	private String token;
	private LocalDateTime tokenExpiredDate;
	private LocalDateTime createDatetime;
	private LocalDateTime updateDatetime;
	private LocalDateTime deleteDatetime;
	
	public RegistTokenInfoDto(RegistTokenInfo entity) {
		BeanCopyUtils.copyProperties(entity, this);
	}
	
	/**
	 * contructor
	 * @param userId
	 * @param token
	 * @param tokenExpiredDay số ngày hết hạn (vd: 1 = 1 ngày)
	 */
	public RegistTokenInfoDto(long userId, String token, long tokenExpiredDay) {
		this.userId = userId;
		this.token = token;
		this.tokenExpiredDate = LocalDateTime.now().plusDays(tokenExpiredDay);
	}
	
	public RegistTokenInfo toRegistTokenInfo() {
		RegistTokenInfo entity = new RegistTokenInfo();
		BeanCopyUtils.copyProperties(this, entity);
		return entity;
	}
}