package com.socialnetwork.general.user.forms;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserLoginForm {
	@NotBlank(message = "Chưa nhập tài khoản")
	@Size(max = 20, message = "Độ dài tài khoản quá 20 ký tự")
	private String username;
	
	@NotBlank(message = "Chưa nhập mật khẩu")
	@Size(min = 6, max = 32, message = "Nhập mật khẩu giới hạn từ 6 đến 32 ký tự")
	private String password;
}
