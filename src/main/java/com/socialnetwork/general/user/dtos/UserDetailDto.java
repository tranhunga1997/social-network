package com.socialnetwork.general.user.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetailDto {
	private String firstName;
	private String lastName;
	private String email;
}
