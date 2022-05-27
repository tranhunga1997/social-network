package com.socialnetwork.general.user.dtos;

import java.time.LocalDateTime;
import java.util.List;

import com.socialnetwork.common.entities.user.PermissionInfo;
import com.socialnetwork.common.entities.user.RoleInfo;
import com.socialnetwork.common.utils.BeanCopyUtils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleDetailInfoDto {
	private Integer id;
	private String slug;
	private String name;
	private List<PermissionInfo> permissions;
	private LocalDateTime createAt;
	
	public RoleDetailInfoDto(RoleInfo entity) {
		BeanCopyUtils.copyProperties(entity, this);
	}
	
	public RoleInfo toRoleInfo() {
		RoleInfo entity = new RoleInfo();
		BeanCopyUtils.copyProperties(this, entity);
		return entity;
	}
	
}
