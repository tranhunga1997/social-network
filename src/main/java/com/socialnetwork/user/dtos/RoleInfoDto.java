package com.socialnetwork.user.dtos;

import java.time.LocalDateTime;
import java.util.List;

import com.socialnetwork.common.entities.user.PermissionInfo;
import com.socialnetwork.common.entities.user.RoleInfo;
import com.socialnetwork.common.utils.BeanCopyUtils;

import lombok.Data;

@Data
public class RoleInfoDto {
	private Integer id;
	private String slug;
	private String name;
	private List<PermissionInfo> permissions;
	private LocalDateTime createAt;
	
	public RoleInfoDto(RoleInfo entity) {
		BeanCopyUtils.copyProperties(entity, this);
	}
	
	public RoleInfo toRoleInfo() {
		RoleInfo entity = new RoleInfo();
		BeanCopyUtils.copyProperties(this, entity);
		return entity;
	}
	
}
