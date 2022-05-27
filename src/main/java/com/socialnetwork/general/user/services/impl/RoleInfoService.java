package com.socialnetwork.general.user.services.impl;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.socialnetwork.common.entities.user.PermissionInfo;
import com.socialnetwork.common.entities.user.RoleInfo;
import com.socialnetwork.common.exceptions.SocialException;
import com.socialnetwork.common.exceptions.SystemException;
import com.socialnetwork.common.repositories.user.PermissionInfoRepository;
import com.socialnetwork.common.repositories.user.RoleInfoRepository;
import com.socialnetwork.common.utils.StringUtil;
import com.socialnetwork.general.user.dtos.PermissionInfoDto;
import com.socialnetwork.general.user.dtos.RoleDetailInfoDto;

/**
 * Class xử lý thông tin role
 * @author Mạnh Hùng
 *
 */
@Service
public class RoleInfoService extends UserServiceBase<RoleDetailInfoDto>{
	@Autowired
	private RoleInfoRepository roleInfoRepository;
	@Autowired
	private PermissionInfoRepository permissionInfoRepository;
	
	/**
	 * Tạo mới role
	 * @param dto
	 * @return thông tin role vừa được tạo
	 */
//	@Override
//	public void create(RoleInfoDto dto) {
//		dto.setSlug(StringUtil.toSlug(dto.getName()));
//		dto.setCreateAt(LocalDateTime.now());
//		RoleInfo entity = dto.toRoleInfo();
//		
//		if(!StringUtil.isNull(dto.getPermissions()) && dto.getPermissions().size() != 0) {
//			List<PermissionInfo> permissionEntityList = new ArrayList<PermissionInfo>();
//			
//			for(PermissionInfoDto permissionDto : dto.getPermissions()) {
//				permissionEntityList.add(permissionDto.toPermissionInfo());
//			}
//			
//			entity.setPermissions(permissionEntityList);
//		}
//		RoleInfo result = roleInfoRepository.save(entity);
//		dto.setId(result.getId());
//	}
	
	/**
	 * Tìm tất cả thông tin role
	 * @param page (bắt đầu từ 0)
	 * @param size
	 * @return tất cả thông tin role
	 */
	public Page<RoleDetailInfoDto> findAll(int page, int size){
		Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
		Page<RoleInfo> rolePage = roleInfoRepository.findAll(pageable);
		List<RoleDetailInfoDto> dtos = new ArrayList<>();
		
		for(RoleInfo role : rolePage) {
			dtos.add(new RoleDetailInfoDto(role));
		}
		
		return new PageImpl<>(dtos, pageable, rolePage.getTotalElements());
		
	}
	
	/**
	 * Tìm thông tin role sử dụng role id
	 * @param roleId
	 * @return thông tin role
	 */
	public RoleDetailInfoDto findById(int roleId) {
		return new RoleDetailInfoDto(roleInfoRepository.findById(roleId).get());
	}
	
	public RoleDetailInfoDto findBySlug(String slug) {
		return new RoleDetailInfoDto(roleInfoRepository.findBySlug(slug));
	}
	
	@Override
	public void create(RoleDetailInfoDto dto) {
		dto.setSlug(StringUtil.toSlug(dto.getName()));
		dto.setCreateAt(LocalDateTime.now());
		RoleInfo entity = dto.toRoleInfo();
		
		if(!StringUtil.isNull(dto.getPermissions()) && dto.getPermissions().size() != 0) {
			entity.setPermissions(dto.getPermissions());
		}
		RoleInfo result = roleInfoRepository.save(entity);
		dto.setId(result.getId());
		
	}

	/**
	 * Sửa permission của role
	 * @param roleId
	 * @return <code>true</code> thành công, <code>false</code> ngược lại
	 */
	@Deprecated
	public boolean update(int roleId, List<Integer> permissionIds) {
		// lấy thông tin và kiểm tra null
		RoleDetailInfoDto roleInfoDto = findById(roleId);
		if(StringUtil.isNull(roleInfoDto)) {
			return false;
		}
		
		List<PermissionInfo> permissions = permissionInfoRepository.findAllById(permissionIds);
		
		// xử lý sửa
		RoleInfo roleInfo = roleInfoDto.toRoleInfo();
		roleInfo.getPermissions().addAll(permissions);
		try {
			return roleInfoRepository.save(roleInfo) != null ? true : false;
		} catch (Exception e) {
			throw new SystemException("E_00009");
		}
	}

	@Override
	public void update(RoleDetailInfoDto dto) {
		// lấy thông tin và kiểm tra null
		RoleDetailInfoDto roleInfoDto = findById(dto.getId());
		if(StringUtil.isNull(roleInfoDto)) {
			throw new SocialException("W_00011", "Sửa role");
		}
		
		roleInfoRepository.save(dto.toRoleInfo());
	}

	@Override
	public void deleteById(Object id) {
		if(!(id instanceof Integer)) {
			throw new InvalidParameterException("param \"id\" is not integer.");
		}
		roleInfoRepository.deleteById((Integer) id);
	}

}
