package com.socialnetwork.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socialnetwork.common.entities.user.PermissionInfo;
import com.socialnetwork.common.repositories.user.PermissionInfoRepository;
import com.socialnetwork.common.utils.StringUtil;
import com.socialnetwork.user.dtos.PermissionInfoDto;

@Service
public class PermissionInfoService {
	
	@Autowired
	private PermissionInfoRepository permissionInfoRepository;
	
	/**
	 * Tạo mới permission
	 * @param dto
	 * @return thông tin permission đã được tạo
	 */
	public PermissionInfoDto create(PermissionInfoDto dto) {
		// tạo slug từ name
		dto.setSlug(StringUtil.toSlug(dto.getName()));
		
		// chuyển dto sang entity
		PermissionInfo entity = dto.toPermissionInfo();
		PermissionInfoDto result = new PermissionInfoDto(permissionInfoRepository.save(entity));
		return result;
	}
	
	
	
}
