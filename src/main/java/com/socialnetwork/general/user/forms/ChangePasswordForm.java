package com.socialnetwork.general.user.forms;

import lombok.Data;

@Data
public class ChangePasswordForm {
	private String password;
	private String newPassword;
	private String newPasswordConfirm;
}
