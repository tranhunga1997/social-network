package com.socialnetwork.general.user.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socialnetwork.common.entities.user.UserInfo;
import com.socialnetwork.common.exceptions.SocialException;
import com.socialnetwork.common.repositories.user.UserRepository;
import com.socialnetwork.common.utils.BeanCopyUtils;
import com.socialnetwork.common.utils.StringUtil;
import com.socialnetwork.general.user.dtos.RegistTokenInfoDto;
import com.socialnetwork.general.user.dtos.UserInfoDto;
/**
 * <b>Tác dụng: </b>xử lý logic bảng thông tin tài khoản.
 * @author Mạnh Hùng
 *
 */
@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RegistTokenService registTokenService;
	
	/**
	 * Tìm tất cả tài khoản
	 * @return
	 */
	public List<UserInfoDto> findAll(){
		List<UserInfo> userInfos = userRepository.findAll();
		List<UserInfoDto> userInfoDtos = new ArrayList<>();
		
		for(UserInfo userInfo : userInfos) {
			userInfoDtos.add(new UserInfoDto(userInfo));
		}
		
		return userInfoDtos;
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
	public UserInfoDto findByUsername(String username) {
		Optional<UserInfo> userOptional = userRepository.findByUsername(username);
		UserInfoDto userInfoDto = null;
		if(userOptional.isPresent()) {
			userInfoDto = new UserInfoDto(userOptional.get());
		}
		return userInfoDto;
	}
	
	/**
	 * Tìm tài khoản theo user id
	 * @param userId
	 * @return thông tin tài khoản
	 */
	public UserInfoDto findById(long userId) {
		Optional<UserInfo> userOptional = userRepository.findById(userId);
		UserInfoDto userInfoDto = null;
		if(userOptional.isPresent()) {
			userInfoDto = new UserInfoDto(userOptional.get());
		}
		return userInfoDto;
	}
	
	/**
	 * Tìm tài khoản theo email
	 * @param email
	 * @return thông tin tài khoản
	 */
	public UserInfoDto findByEmail(String email) {
		Optional<UserInfo> userOptional = userRepository.findByEmail(email);
		
		UserInfoDto userInfoDto = null;
		if(userOptional.isPresent()) {
			userInfoDto = new UserInfoDto(userOptional.get());
		}
		return userInfoDto;
	}
	/**
	 * Tạo mới tài khoản
	 * @param UserInfoDto
	 * @return UserInfoDto
	 */
	public UserInfoDto create(UserInfoDto dto) {
		if(StringUtil.isNull(dto.getUsername())) {
			throw new SocialException("W_00002");
		}
		
		UserInfo userInfo = dto.toUserInfo();
		return new UserInfoDto(userRepository.save(userInfo));
	}
	/**
	 * Sửa tài khoản
	 * <b>Điều kiện: </b>phải có user id 
	 * @param UserInfoDto
	 * @return UserInfoDto
	 */
	public UserInfoDto update(UserInfoDto dto) {
		if(StringUtil.isNull(dto.getUsername())) {
			throw new SocialException("W_00002");
		}
		
		UserInfo userInfo = dto.toUserInfo();
		return new UserInfoDto(userRepository.save(userInfo));
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
}
