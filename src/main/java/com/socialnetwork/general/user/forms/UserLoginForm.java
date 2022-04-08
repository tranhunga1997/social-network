package com.socialnetwork.general.user.forms;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserLoginForm {
	@NotBlank
	@Size(max = 20)
	private String username;
	
	@NotBlank
	@Size(min = 6, max = 32)
	private String password;
}
