package com.socialnetwork.general.user.forms;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ForgetPasswordForm {
	@NotBlank
	@Size(min = 6, max = 32)
	private String newPassword;
	
	@NotBlank
	@Size(min = 6, max = 32)
	private String newPasswordConfirm;
	
	@Pattern(regexp = "^\\d{6}$")
	private String token;
	
}
