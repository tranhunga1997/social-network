package com.socialnetwork.general.user.dtos;

import com.socialnetwork.common.entities.user.PermissionInfo;
import com.socialnetwork.common.utils.BeanCopyUtils;

import lombok.Data;

@Data
public class PermissionInfoDto {
	private Integer id;
	private String slug;
	private String name;
	
	public PermissionInfoDto(PermissionInfo entity) {
		BeanCopyUtils.copyProperties(entity, this);
	}
	
	public PermissionInfo toPermissionInfo() {
		PermissionInfo entity = new PermissionInfo();
		BeanCopyUtils.copyProperties(this, entity);
		return entity;
	}
}
