package com.socialnetwork.general.user.dtos;

import java.time.LocalDateTime;
import java.util.List;

import com.socialnetwork.common.entities.user.RoleInfo;
import com.socialnetwork.common.entities.user.UserInfo;
import com.socialnetwork.common.utils.BeanCopyUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * UserInfoDto
 * @author Mạnh Hùng
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailInfoDto {
	private Long userId;
	private String username;
	private String lastName;
	private String firstName;
	private String email;
	private List<RoleInfo> roles;
	private boolean isEnabled;
	private boolean isBlocked;
	private LocalDateTime createDatetime;
	private LocalDateTime updateDatetime;
	private LocalDateTime deleteDatetime;
	
	/**
	 * Constructor
	 * @param userInfo
	 */
	public UserDetailInfoDto(UserInfo userInfo) {
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
