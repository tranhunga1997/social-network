package com.socialnetwork.general.user.forms;

import java.util.List;

import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class RoleCreateForm {
	@Pattern(regexp = "^[^\\d]$")
	private String name;
	private List<String> permissions;
}
