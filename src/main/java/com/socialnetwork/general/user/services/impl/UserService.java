package com.socialnetwork.general.user.services.impl;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.socialnetwork.common.entities.user.ForgetPwdTokenInfo;
import com.socialnetwork.common.entities.user.PermissionInfo;
import com.socialnetwork.common.entities.user.UserInfo;
import com.socialnetwork.common.exceptions.SocialException;
import com.socialnetwork.common.repositories.user.UserRepository;
import com.socialnetwork.common.utils.StringUtil;
import com.socialnetwork.general.user.dtos.RegistTokenInfoDto;
import com.socialnetwork.general.user.dtos.UserDetailInfoDto;
/**
 * <b>Tác dụng: </b>xử lý logic bảng thông tin tài khoản.
 * @author Mạnh Hùng
 *
 */
@Service
@Transactional
public class UserService extends UserServiceBase<UserDetailInfoDto> {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RegistTokenService registTokenService;
	
	/**
	 * Tìm tất cả tài khoản
	 * @return
	 */
	public Page<UserDetailInfoDto> findAll(Pageable pageable) {
		Page<UserInfo> pageUserInfo = super.findAll(pageable, UserInfo.class);
		
		List<UserDetailInfoDto> userInfoDtos = new ArrayList<UserDetailInfoDto>();
		pageUserInfo.toList().forEach(item -> {
			UserDetailInfoDto dto = new UserDetailInfoDto(item);
			userInfoDtos.add(dto);
		});
		
		return new PageImpl<UserDetailInfoDto>(userInfoDtos, pageable, pageUserInfo.getTotalElements());
	}
	
	/**
	 * Tìm kiếm theo filter
	 * @param conditionObj
	 * @param pageable
	 * @return
	 */
	public Page<UserDetailInfoDto> find(Object conditionObj, Pageable pageable) {
		Page<UserInfo> pageUserInfo = super.find(conditionObj, pageable, UserInfo.class);
		List<UserDetailInfoDto> userInfoDtos = new ArrayList<UserDetailInfoDto>();
		pageUserInfo.toList().forEach(item -> {
			UserDetailInfoDto dto = new UserDetailInfoDto(item);
			userInfoDtos.add(dto);
		});
		
		return new PageImpl<UserDetailInfoDto>(userInfoDtos, pageable, pageUserInfo.getTotalElements());
	}

	/**
	 * Tìm user id
	 * @param username
	 * @return user id
	 */
	public long getUserId(String username) {
		return userRepository.getUserId(username);
	}
	
	/**
	 * Lấy thông tin email
	 * @param username
	 * @return email
	 */
	public String getEmail(String username) {
		return findByUsername(username).getEmail();
	}
	
	/**
	 * Lấy thông tin email
	 * @param userId
	 * @return email
	 */
	public String getEmail(long userId) {
		return findById(userId).getEmail();
	}
	
	/**
	 * Tìm tài khoản theo username
	 * @param username
	 * @return thông tin tài khoản
	 */
	public UserDetailInfoDto findByUsername(String username) {
		Optional<UserInfo> userOptional = userRepository.findByUsername(username);
		UserDetailInfoDto userInfoDto = null;
		
		if(userOptional.isEmpty()) {
			throw new SocialException("E_00003");
		}
		
		userInfoDto = new UserDetailInfoDto(userOptional.get());
		return userInfoDto;
	}
	
	/**
	 * Tìm tài khoản theo user id
	 * @param userId
	 * @return thông tin tài khoản
	 */
	public UserDetailInfoDto findById(long userId) {
		Optional<UserInfo> userOptional = userRepository.findById(userId);
		UserDetailInfoDto userInfoDto = null;
		if(userOptional.isPresent()) {
			userInfoDto = new UserDetailInfoDto(userOptional.get());
		}
		return userInfoDto;
	}
	
	/**
	 * Tìm tài khoản theo email
	 * @param email
	 * @return thông tin tài khoản
	 */
	public UserDetailInfoDto findByEmail(String email) {
		Optional<UserInfo> userOptional = userRepository.findByEmail(email);
		
		UserDetailInfoDto userInfoDto = null;
		if(userOptional.isPresent()) {
			userInfoDto = new UserDetailInfoDto(userOptional.get());
		}
		return userInfoDto;
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
	
	/**
	 * Kiểm tra tồn tại tài khoản
	 * @param userId
	 * @return <code>true</code> tồn tại, <code>false</code> ngược lại
	 */
	public boolean isExists(long userId) {
		return userRepository.existsById(userId);
	}
	
	/**
	 * Kiểm tra tồn tại tài khoản
	 * @param userId
	 * @return <code>true</code> tồn tại, <code>false</code> ngược lại
	 */
	public boolean isExists(String username) {
		return userRepository.existsByUsername(username);
	}
	
	public boolean isExistsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}
	/**
	 * Kích hoạt tài khoản
	 * @param registToken
	 * @param userId
	 * @return <code>true</code> kích hoạt thành công, <code>false</code> ngược lại
	 */
	public boolean active(String registToken, long userId) {
		RegistTokenInfoDto registTokenInfoDto = registTokenService.findByToken(registToken);
		// so sánh token đúng với user id
		if(registTokenInfoDto.getUserId() != userId) {
			return false;
		}
		
		// kích hoạt tài khoản
		userRepository.activeById(userId);
		
		// xóa logic regist token
		registTokenService.deleteLogic(userId);
		return true;
	}

	/**
	 * Sửa tài khoản
	 * <b>Điều kiện: </b>phải có user id 
	 * @param UserDetailInfoDto
	 * @return UserInfoDto
	 */
//	@Override
//	public void update(UserInfoDto dto) {
//		if(StringUtil.isNull(dto.getUserId()) {
//			throw new SocialException("W_00002");
//		}
//		
//		UserInfo userInfo = dto.toUserInfo();
//		userRepository.save(userInfo);
//	}

	@Override
	public void deleteById(Object id) {
		userRepository.deleteById((Long) id);
	}

	/**
	 * Tạo mới tài khoản
	 * @param UserDetailInfoDto
	 * @return UserInfoDto
	 */
	@Override
	public void create(UserDetailInfoDto dto) {
		if(StringUtil.isNull(dto.getUsername())) {
			throw new SocialException("W_00002");
		}
		
		UserInfo userInfo = userRepository.save(dto.toUserInfo());
		dto.setUserId(userInfo.getUserId());
	}

	/**
	 * Sửa tài khoản
	 * <b>Điều kiện: </b>phải có user id 
	 * @param UserDetailInfoDto
	 * @return UserInfoDto
	 */
	@Override
	public void update(UserDetailInfoDto dto) {
		if(StringUtil.isNull(dto.getUserId())) {
			throw new SocialException("W_00002");
		}
		
		UserInfo userInfo = userRepository.save(dto.toUserInfo());
		dto.setUserId(userInfo.getUserId());
	}

}
