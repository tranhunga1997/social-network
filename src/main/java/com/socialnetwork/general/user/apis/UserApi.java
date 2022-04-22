package com.socialnetwork.general.user.apis;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.socialnetwork.common.entities.user.RoleInfo;
import com.socialnetwork.common.exceptions.InputException;
import com.socialnetwork.common.exceptions.SocialException;
import com.socialnetwork.common.utils.MailUtil;
import com.socialnetwork.common.utils.StringUtil;
import com.socialnetwork.common.utils.TokenProvider;
import com.socialnetwork.general.user.dtos.AuthenticateInfoDto;
import com.socialnetwork.general.user.dtos.LoginHistoryInfoDto;
import com.socialnetwork.general.user.dtos.LoginResponseData;
import com.socialnetwork.general.user.dtos.LoginTokenInfoDto;
import com.socialnetwork.general.user.dtos.RegistTokenInfoDto;
import com.socialnetwork.general.user.dtos.RoleInfoDto;
import com.socialnetwork.general.user.dtos.UserDetailDto;
import com.socialnetwork.general.user.dtos.UserInfoDto;
import com.socialnetwork.general.user.forms.ChangePasswordForm;
import com.socialnetwork.general.user.forms.UserInfoUpdateForm;
import com.socialnetwork.general.user.forms.UserLoginForm;
import com.socialnetwork.general.user.forms.UserRegisterForm;
import com.socialnetwork.general.user.services.AuthenticateService;
import com.socialnetwork.general.user.services.LoginHistoryService;
import com.socialnetwork.general.user.services.LoginTokenInfoService;
import com.socialnetwork.general.user.services.RegistTokenService;
import com.socialnetwork.general.user.services.RoleInfoService;
import com.socialnetwork.general.user.services.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin("*")
@RestController
@RequestMapping("api/user")
@Slf4j
public class UserApi {
	@Autowired
	UserService userService;
	@Autowired
	AuthenticateService authService;
	@Autowired
	RoleInfoService roleInfoService;
	@Autowired
	RegistTokenService registTokenService;
	@Autowired
	LoginTokenInfoService loginTokenInfoService;
	@Autowired
	LoginHistoryService loginHistoryService;
	
	/* ============================================================================================ */
	/**
	 * Controller đăng ký tài khoản
	 * @param req httpRequest
	 * @param form biểu mẫu đăng ký
	 * @return http code
	 */
	@ApiOperation(value = "Đăng ký tài khoản")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại")
	})
	@PostMapping("/register")
	ResponseEntity<?> create(HttpServletRequest req, @Valid UserRegisterForm form){
		// kiểm tra xác nhận mật khẩu
		if(!Objects.equals(form.getPassword(), form.getPasswordConfirm())) {
			throw new InputException("xác nhận mật khẩu", "E_00012", "mật khẩu", "xác nhận mật khẩu");
		}
		// kiểm tra tài khoản tồn tại
		boolean checkExists = userService.isExists(form.getEmail());
		if(checkExists) {
			throw new SocialException("W_00011", "Tạo tài khoản");
		}
		//lấy thông tin role user
		List<RoleInfo> roleInfos = new ArrayList<>(); 
		RoleInfoDto roleInfoDto = roleInfoService.findById(1); // lấy thông tin role user
		if(StringUtil.isNull(roleInfoDto)) {
			throw new SocialException("E_00003");
		}
		roleInfos.add(roleInfoDto.toRoleInfo());
		
		// tạo tài khoản
		UserInfoDto userInfoDto = form.toUserInfoDto();
		userInfoDto.setRoles(roleInfos);
		long userId = userService.create(userInfoDto).getUserId();
		
		// tạo mật khẩu
		authService.create(userId, form.getPassword());
		
		// tạo token
		long registTokenExpiryDays = 1L;
		RegistTokenInfoDto registTokenInfoDto = new RegistTokenInfoDto(userId, TokenProvider.generateToken(), registTokenExpiryDays);
		String token = registTokenService.create(registTokenInfoDto).getToken();
		
		// gửi mail
		String clientHost = req.getLocalName();
		MailUtil.sendTextMail(form.getEmail(), "Kích hoạt tài khoản", clientHost+"/api/user/register-active?token="+token);
		
		return ResponseEntity.ok().build();
	}
	
	/**
	 * Controller đăng nhập
	 * @return http code
	 */
	@ApiOperation(value = "Đăng nhập tài khoản")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại")
	})
	@PostMapping("/login")
	ResponseEntity<LoginResponseData> login(HttpServletRequest req, @Valid UserLoginForm form){
		String ipAddress = req.getRemoteAddr();
		
		// kiểm tra tài khoản
		String username = form.getUsername();
		String password = form.getPassword();
		UserInfoDto userInfoDto = userService.findByUsername(username);
		AuthenticateInfoDto authenticateInfoDto = authService.findNewInfo(userInfoDto.getUserId());
		
		// xác thực tài khoản
		int authCode = authService.authentication(userInfoDto, authenticateInfoDto, password);
		switch (authCode) {
		case -1:
			// tài khoản không tồn tại
			throw new SocialException("W_00011", "Đăng nhập"); 
		case 0: 
			// thành công
			break;
		case 1:
			 // tài khoản chưa kích hoạt
			throw new SocialException("W_00014");
		case 2:
			// tài khoản bị khóa
			throw new SocialException("W_00015"); 
		case 3:
			// quá số lần đăng nhập
			userService.blockAndUnblock(username, false);
			throw new SocialException("W_00013"); 
		case 4:
			// sai mật khẩu
			authService.loginFailedAction(userInfoDto.getUserId(), authenticateInfoDto.getLoginFailedCounter());
			throw new SocialException("W_00011", "Đăng nhập"); 
		}
		
		// khởi tạo jwt và refresh token
		LoginTokenInfoDto loginTokenInfoDto = loginTokenInfoService.create(userInfoDto.getUserId(), ipAddress);
		String jwt = TokenProvider.generateJwt(username, loginTokenInfoDto.getRefreshId());
		
		// ghi lại lịch sử đăng nhập
		LoginHistoryInfoDto loginHistoryInfoDto = new LoginHistoryInfoDto();
		loginHistoryInfoDto.setAccessAt(LocalDateTime.now());
		loginHistoryInfoDto.setIpAddress(ipAddress);
		loginHistoryInfoDto.setUserId(userInfoDto.getUserId());
		loginHistoryService.create(loginHistoryInfoDto);
		
		// trả về jwt và refresh token
		return ResponseEntity.ok(new LoginResponseData(jwt, loginTokenInfoDto.getRefreshToken()));
	}
	
	/**
	 * Controller đăng xuất
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "Đăng xuất tài khoản")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại")
	})
	@PostMapping("/logout")
	ResponseEntity<?> logout(HttpServletRequest request){
		long refreshId = TokenProvider.getRefreshTokenIdFromRequest(request);
		loginTokenInfoService.delete(refreshId);
		
		return ResponseEntity.ok().build();
	}
	
	/**
	 * Controller thông tin chi tiết
	 * @param request
	 * @param username
	 * @return
	 */
	@ApiOperation(value = "Xem thông tin chi tiết")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại")
	})
	@GetMapping("/detail/{username}")
	ResponseEntity<UserDetailDto> viewDetail(HttpServletRequest request, @PathVariable String username) {
		String usernameLocal = TokenProvider.getUserUsernameFromRequest(request);
		
		// Kiểm tra thông tin tài khoản muốn lấy có hợp lệ
		if(!username.equals(usernameLocal)) {
			log.debug("Lấy dữ liệu tài khoản " + username + " thất bại");
			throw new SocialException("W_00011", "lấy dữ liệu");
		}
		
		// lấy dữ liệu tài khoản
		UserInfoDto userInfoDto = userService.findByUsername(usernameLocal);
		
		// khởi tạo dữ liệu cần thiết trả về
		UserDetailDto userDetailDto = UserDetailDto.builder()
				.firstName(userInfoDto.getFirstName())
				.lastName(userInfoDto.getLastName())
				.email(userInfoDto.getEmail())
				.build();
		return ResponseEntity.ok(userDetailDto);
	}
	
	/**
	 * Controller sửa thông tin chi tiết tài khoản
	 * @param request
	 * @param username
	 * @param form
	 * @return
	 */
	@ApiOperation(value = "Sửa thông tin chi tiết")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại")
	})
	@PutMapping("detail/{username}")
	ResponseEntity<UserDetailDto> updateDetail(HttpServletRequest request, @PathVariable String username, UserInfoUpdateForm form) {
		String usernameLocal = TokenProvider.getUserUsernameFromRequest(request);
		
		// Kiểm tra thông tin tài khoản muốn lấy có hợp lệ
		if(!username.equals(usernameLocal)) {
			log.debug("Lấy dữ liệu tài khoản " + username + " thất bại");
			throw new SocialException("W_00011", "lấy dữ liệu");
		}
		
		// lấy thông tin user
		UserInfoDto userDto = userService.findByUsername(username);
		
		userDto.setEmail(form.getEmail());
		userDto.setFirstName(form.getFirstName());
		userDto.setLastName(form.getLastName());
		userDto.setUpdateDatetime(LocalDateTime.now());
		
		UserInfoDto uDto = userService.create(userDto);
		
		UserDetailDto resultDto = UserDetailDto.builder()
				.email(uDto.getEmail())
				.firstName(uDto.getFirstName())
				.lastName(uDto.getLastName())
				.build();
		
		return ResponseEntity.ok(resultDto);
	}
	
	@PostMapping("password/change")
	ResponseEntity<String> changePassword(HttpServletRequest request, ChangePasswordForm form) {
		String username = TokenProvider.getUserUsernameFromRequest(request);
		UserInfoDto userInfoDto = userService.findByUsername(username);
		
		// kiểm tra tồn tại tài khoản
		if (StringUtil.isNull(userInfoDto)) {
			throw new SocialException("E_00003");
		}
		
		// kiểm tra mật khẩu mới không trùng mật khẩu cũ
		if (Objects.equals(form.getPassword(), form.getNewPassword())) {
			throw new InputException("mật khẩu", "W_00019", "Mật khẩu cũ", "mật khẩu mới");
		}
		
		// kiểm tra xác nhận mật khẩu
		if (!Objects.equals(form.getNewPassword(), form.getNewPasswordConfirm())) {
			throw new InputException("xác nhận mật khẩu", "E_00012", "mật khẩu", "xác nhận mật khẩu");
		}
		
		// xử lý thay đổi mật khẩu
		authService.update(userInfoDto.getUserId(), form.getNewPassword());
		
		// gửi email thông báo
		MailUtil.sendTextMail(userInfoDto.getEmail(), "Thông báo thay đổi mật khẩu", "Mật khẩu của tài khoản (" + username + ") đã được thay đổi.");
		
		return ResponseEntity.ok("Thành công");
	}
}
