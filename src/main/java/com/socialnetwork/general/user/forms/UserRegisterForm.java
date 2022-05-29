package com.socialnetwork.general.user.forms;

import java.time.LocalDateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.socialnetwork.common.utils.BeanCopyUtils;
import com.socialnetwork.general.user.dtos.UserDetailInfoDto;

import lombok.Data;

@Data
public class UserRegisterForm {
	@NotBlank
	@Size(max = 20)
	private String username;
	
	@NotBlank
	@Size(min = 6, max = 32)
	private String password;
	
	@NotBlank
	@Size(min = 6, max = 32)
	private String passwordConfirm;
	
	@Pattern(regexp = "^[^\\d]+$")
	@Size(max = 20)
	private String lastName;
	
	@Pattern(regexp = "^[^\\d]+$")
	@Size(max = 20)
	private String firstName;
	
	@Email
	@Size(max = 40)
	private String email;
	
	public UserDetailInfoDto toUserInfoDto() {
		UserDetailInfoDto dto = new UserDetailInfoDto();
		BeanCopyUtils.copyProperties(this, dto);
		dto.setCreateDatetime(LocalDateTime.now());
		dto.setBlocked(false);
		dto.setEnabled(false);
		return dto;
	}
}
