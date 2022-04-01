package com.socialnetwork.user.dtos;

import java.time.LocalDateTime;
import java.util.List;

import com.socialnetwork.common.entities.RoleInfo;
import com.socialnetwork.common.entities.UserInfo;
import com.socialnetwork.common.utils.BeanCopyUtils;

import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * UserInfoDto
 * @author Mạnh Hùng
 *
 */
@Data
@NoArgsConstructor
public class UserInfoDto {
	private String username;
	private String lastName;
	private String firstName;
	private String email;
	private List<RoleInfo> roles;
	private boolean enable;
	private boolean block;
	private LocalDateTime createDatetime;
	private LocalDateTime updateDatetime;
	private LocalDateTime deleteDatetime;
	
	/**
	 * Constructor
	 * @param userInfo
	 */
	public UserInfoDto(UserInfo userInfo) {
		BeanCopyUtils.copyProperties(userInfo, this);
	}
	
	/**
	 * Chuyển dữ liệu từ user dto sang user info
	 * @return UserInfo
	 */
	public UserInfo toUserInfo() {
		UserInfo userInfo = new UserInfo();
		BeanCopyUtils.copyProperties(this, userInfo);
		return userInfo;
	}
}
