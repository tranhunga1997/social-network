package com.socialnetwork.user.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socialnetwork.common.entities.UserInfo;
import com.socialnetwork.common.exceptions.SocialException;
import com.socialnetwork.common.repositories.UserRepository;
import com.socialnetwork.common.utils.BeanCopyUtils;
import com.socialnetwork.common.utils.StringUtil;
import com.socialnetwork.user.dtos.UserInfoDto;
/**
 * <b>Tác dụng: </b>xử lý logic bảng thông tin tài khoản.
 * @author Mạnh Hùng
 *
 */
@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * Tìm tất cả tài khoản
	 * @return
	 */
	public List<UserInfoDto> findAll(){
		List<UserInfo> userInfos = userRepository.findAll();
		List<UserInfoDto> userInfoDtos = new ArrayList<>();
		
		for(UserInfo userInfo : userInfos) {
			UserInfoDto userInfoDto = new UserInfoDto();
			BeanCopyUtils.copyProperties(userInfo, userInfoDto);
			userInfoDtos.add(userInfoDto);
		}
		
		return userInfoDtos;
	}
	public long getUserId(String username) {
		return userRepository.getUserId(username);
	}
	/**
	 * Tìm tài khoản theo id
	 * @param id
	 * @return
	 */
	public UserInfoDto findById(long id) {
		Optional<UserInfo> userOptional = userRepository.findById(id);
		UserInfoDto userInfoDto = new UserInfoDto();
		if(userOptional.isEmpty()) {
			BeanCopyUtils.copyProperties(userOptional.get(), userInfoDto);
		}else {
			userInfoDto = null;
		}
		return userInfoDto;
	}
	/**
	 * Tìm tài khoản theo username
	 * @param username
	 * @return
	 */
	public UserInfoDto findByUsername(String username) {
		Optional<UserInfo> userOptional = userRepository.findByUsername(username);
		UserInfoDto userInfoDto = new UserInfoDto();
		if(userOptional.isEmpty()) {
			BeanCopyUtils.copyProperties(userOptional.get(), userInfoDto);
		}else {
			userInfoDto = null;
		}
		return userInfoDto;
	}
	/**
	 * Tạo mới tài khoản
	 * @param UserInfoDto
	 * @return UserInfoDto
	 */
	public UserInfoDto create(UserInfoDto dto) {
		if(!StringUtil.isNull(dto.getId())) {
			throw new SocialException("W_00002");
		}
		
		UserInfo userInfo = new UserInfo();
		BeanCopyUtils.copyProperties(dto, userInfo);
		long id = userRepository.save(userInfo).getId();
		dto.setId(id);
		return dto;
	}
	/**
	 * Sửa tài khoản
	 * <b>Điều kiện: </b>phải có user id 
	 * @param UserInfoDto
	 * @return UserInfoDto
	 */
	public UserInfoDto update(UserInfoDto dto) {
		if(StringUtil.isNull(dto.getId())) {
			throw new SocialException("W_00002");
		}
		
		UserInfo userInfo = new UserInfo();
		BeanCopyUtils.copyProperties(dto, userInfo);
		long id = userRepository.save(userInfo).getId();
		dto.setId(id);
		return dto;
	}
	/**
	 * Kích hoạt tài khoản
	 * @param username
	 */
	public void activeUser(String username) {
		userRepository.activeByUsername(username);
	}
	
	/**
	 * Khóa và mở khóa user
	 * <pre>
	 * ví dụ:
	 * tài khoản đã khóa -> mở khóa
	 * tài khoản không bị khóa -> khóa
	 * </pre>
	 * @param username
	 * @param status (trạng thái block hiện tại)
	 */
	public void blockAndUnblock(String username, boolean status) {
		userRepository.blockAndUnblock(username, !status);
	}
	
	// thay đổi mail
	public void changeMail(String email) {
		// viết sau khi hoàn thành gửi mail
	}
	
}
