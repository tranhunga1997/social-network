package com.socialnetwork.general.user.forms;

import java.util.List;

import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class RoleCreateForm {
	@Pattern(regexp = "^[^\\d]+$", message = "Ký tự đã nhập không hợp lệ (chỉ được nhập chữ)")
	private String name;
	private List<Integer> permissionIds;
}
