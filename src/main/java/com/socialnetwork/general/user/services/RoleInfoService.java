package com.socialnetwork.general.user.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.socialnetwork.common.entities.user.RoleInfo;
import com.socialnetwork.common.exceptions.SystemException;
import com.socialnetwork.common.repositories.user.RoleInfoRepository;
import com.socialnetwork.common.utils.StringUtil;
import com.socialnetwork.general.user.dtos.RoleInfoDto;

/**
 * Class xử lý thông tin role
 * @author Mạnh Hùng
 *
 */
@Service
public class RoleInfoService {
	@Autowired
	private RoleInfoRepository roleInfoRepository;
	
	/**
	 * Tạo mới role
	 * @param dto
	 * @return thông tin role vừa được tạo
	 */
	public void create(RoleInfoDto dto) {
		dto.setSlug(StringUtil.toSlug(dto.getName()));
		dto.setCreateAt(LocalDateTime.now());
		RoleInfo entity = dto.toRoleInfo();
		RoleInfo result = roleInfoRepository.save(entity);
		dto.setId(result.getId());
	}
	
	/**
	 * Tìm tất cả thông tin role
	 * @param page (bắt đầu từ 0)
	 * @param size
	 * @return tất cả thông tin role
	 */
	public Page<RoleInfo> findAll(int page, int size){
		return roleInfoRepository.findAll(PageRequest.of(page, size, Sort.by("role_id")));
		
	}
	
	/**
	 * Tìm thông tin role sử dụng role id
	 * @param roleId
	 * @return thông tin role
	 */
	public RoleInfoDto findById(int roleId) {
		return new RoleInfoDto(roleInfoRepository.findById(roleId).get());
	}
	
	/**
	 * Sửa permission của role
	 * @param roleId
	 * @return <code>true</code> thành công, <code>false</code> ngược lại
	 */
	public boolean update(int roleId) {
		// lấy thông tin và kiểm tra null
		RoleInfoDto roleInfoDto = findById(roleId);
		if(StringUtil.isNull(roleInfoDto)) {
			return false;
		}
		
		// xử lý sửa
		try {
			return roleInfoRepository.save(roleInfoDto.toRoleInfo()) != null ? true : false;
		} catch (Exception e) {
			throw new SystemException("E_00009");
		}
	}
	
	/**
	 * Xóa thông tin role
	 * @param roleId
	 */
	public void delete(int roleId) {
		roleInfoRepository.deleteById(roleId);
	}
}
