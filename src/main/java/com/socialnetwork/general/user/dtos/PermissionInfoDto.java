package com.socialnetwork.general.user.dtos;

import com.socialnetwork.common.entities.user.PermissionInfo;
import com.socialnetwork.common.utils.BeanCopyUtils;
import com.vvt.jpa.query.ConditionType;
import com.vvt.jpa.query.SearchColumn;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PermissionInfoDto {
	@SearchColumn(condition = ConditionType.GT)
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
