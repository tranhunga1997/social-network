package com.socialnetwork.general.user.services;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.socialnetwork.common.entities.user.PermissionInfo;
import com.socialnetwork.common.repositories.user.PermissionInfoRepository;
import com.socialnetwork.common.utils.StringUtil;
import com.socialnetwork.general.user.dtos.PermissionInfoDto;

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
	
	/**
	 * Tìm tất cả permission
	 * @param page trang hiện tại
	 * @param size số lượng element mỗi trang
	 * @return danh sách perrmision
	 */
	public Page<PermissionInfoDto> findAll(int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
		Page<PermissionInfo> entitiePage = permissionInfoRepository.findAll(pageable);
		List<PermissionInfoDto> dtos = new ArrayList<>();
		for(PermissionInfo entity : entitiePage) {
			dtos.add(new PermissionInfoDto(entity));
		}
		return new PageImpl<>(dtos, pageable, entitiePage.getTotalElements());
	}
	
	/**
	 * Tìm theo tên permission
	 * @param name tên permission
	 * @return danh sách perrmission
	 */
	public List<PermissionInfoDto> findByName(String name) {
		List<PermissionInfo> entities = permissionInfoRepository.findByNameLike(name);
		List<PermissionInfoDto> dtos = new ArrayList<>();
		for(PermissionInfo entity : entities) {
			dtos.add(new PermissionInfoDto(entity));
		}
		
		return dtos;
	}
	
	public List<PermissionInfoDto> findBySlugIn(List<String> slug) {
		List<PermissionInfo> entities = permissionInfoRepository.findBySlugIn(slug);
		List<PermissionInfoDto> dtos = new ArrayList<>();
		
		for(PermissionInfo entity : entities) {
			dtos.add(new PermissionInfoDto(entity));
		}
		return dtos;
	}
	
}
