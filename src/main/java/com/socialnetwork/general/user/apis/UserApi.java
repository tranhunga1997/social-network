package com.socialnetwork.general.user.apis;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.socialnetwork.common.dtos.Pagination;
import com.socialnetwork.common.entities.user.PermissionInfo;
import com.socialnetwork.common.entities.user.RoleInfo;
import com.socialnetwork.common.exceptions.InputException;
import com.socialnetwork.common.exceptions.SocialException;
import com.socialnetwork.common.utils.MailUtil;
import com.socialnetwork.common.utils.StringUtil;
import com.socialnetwork.common.utils.TokenProvider;
import com.socialnetwork.general.user.dtos.AuthenticateInfoDto;
import com.socialnetwork.general.user.dtos.ForgetPwdTokenInfoDto;
import com.socialnetwork.general.user.dtos.LoginHistoryInfoDto;
import com.socialnetwork.general.user.dtos.LoginResponseData;
import com.socialnetwork.general.user.dtos.LoginTokenInfoDto;
import com.socialnetwork.general.user.dtos.PermissionInfoDto;
import com.socialnetwork.general.user.dtos.RegistTokenInfoDto;
import com.socialnetwork.general.user.dtos.RoleInfoDto;
import com.socialnetwork.general.user.dtos.UserDetailDto;
import com.socialnetwork.general.user.dtos.UserInfoDto;
import com.socialnetwork.general.user.forms.ChangePasswordForm;
import com.socialnetwork.general.user.forms.ForgetPasswordForm;
import com.socialnetwork.general.user.forms.RoleCreateForm;
import com.socialnetwork.general.user.forms.UserInfoUpdateForm;
import com.socialnetwork.general.user.forms.UserLoginForm;
import com.socialnetwork.general.user.forms.UserRegisterForm;
import com.socialnetwork.general.user.services.impl.AuthenticateService;
import com.socialnetwork.general.user.services.impl.ForgetPwdTokenInfoService;
import com.socialnetwork.general.user.services.impl.LoginHistoryService;
import com.socialnetwork.general.user.services.impl.LoginTokenInfoService;
import com.socialnetwork.general.user.services.impl.PermissionInfoService;
import com.socialnetwork.general.user.services.impl.RegistTokenService;
import com.socialnetwork.general.user.services.impl.RoleInfoService;
import com.socialnetwork.general.user.services.impl.UserService;

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
	@Autowired
	ForgetPwdTokenInfoService forgetPwdTokenInfoService;
	@Autowired
	PermissionInfoService permissionInfoService;
	
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
	
	/**
	 * Controller thay đổi mật khẩu
	 * @param request HttpServletRequest
	 * @param form ChangePasswordForm
	 * @return
	 */
	@ApiOperation(value = "Thay đổi mật khẩu")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại hoặc danh sách lỗi")
	})
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
			throw new InputException("xác nhận mật khẩu", "W_00012", "mật khẩu", "xác nhận mật khẩu");
		}
		
		// xử lý thay đổi mật khẩu
		authService.update(userInfoDto.getUserId(), form.getNewPassword());
		
		// gửi email thông báo
		MailUtil.sendTextMail(userInfoDto.getEmail(), "Thông báo thay đổi mật khẩu", "Mật khẩu của tài khoản (" + username + ") đã được thay đổi.");
		
		return ResponseEntity.ok("Thành công");
	}
	
	/**
	 * Controller quên mật khẩu (nhập email)
	 * @param request
	 * @param email
	 * @return
	 */
	@ApiOperation(value = "Quên mật khẩu (nhập email)")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại")
	})
	@PostMapping("forget-password")
	ResponseEntity<String> forgetPasswordEmailInput(HttpServletRequest request, String email) {
		UserInfoDto userInfoDto = userService.findByEmail(email);
		// kiểm tra email tồn tại
		if (StringUtil.isNull(userInfoDto)) {
			throw new SocialException("E_00003");
		}
		
		// khởi tạo token 
		ForgetPwdTokenInfoDto forgetPwdTokenInfoDto = forgetPwdTokenInfoService.create(userInfoDto.getUserId());
		// gửi mail
		MailUtil.sendTextMail(email, "Quên mật khẩu", "Mã xác nhận: " + forgetPwdTokenInfoDto.getToken());
		return ResponseEntity.ok("Thành công");
	}
	
	/**
	 * Controller quên mật khẩu (nhập mật khẩu)
	 * @param request
	 * @param form
	 * @return
	 */
	@ApiOperation(value = "Quên mật khẩu (nhập mật khẩu)")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại")
	})
	@PutMapping("forget-password")
	ResponseEntity<String> forgetPassword(HttpServletRequest request, @Valid ForgetPasswordForm form) {
		// kiểm tra xác nhận mật khẩu mới
		if(!Objects.equals(form.getNewPassword(), form.getNewPasswordConfirm())) {
			throw new InputException("xác nhận mật khẩu mới", "W_00012", "mật khẩu mới", "xác nhận mật khẩu mới");
		}
		
		// kiểm tra token
		ForgetPwdTokenInfoDto forgetPwdTokenInfoDto = forgetPwdTokenInfoService.find(form.getToken());
		
		if(forgetPwdTokenInfoDto.getTokenExpiredAt().isBefore(LocalDateTime.now())) {
			// kiểm tra hiệu lực
			throw new SocialException("W_00021", "token");
		}
		
		// thay đổi mật khẩu
		authService.update(forgetPwdTokenInfoDto.getUserId(), form.getNewPassword());
		// xóa token (dự định không xóa)
		return ResponseEntity.ok("Thành công");
	}
	
	/**
	 * Controller xem quyền hạn
	 * @return
	 */
	@ApiOperation(value = "Xem tất cả permission (quyền hạn)")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại")
	})
	@GetMapping("permission")
	ResponseEntity<Pagination<PermissionInfoDto>> viewPermission(@RequestParam(defaultValue = "1") int page){
		Pagination<PermissionInfoDto> pagination = new Pagination<>();
		Page<PermissionInfoDto> dataPage = permissionInfoService.findAll(page-1, 5);
		
		pagination.setTotalElements(dataPage.getTotalElements());
		pagination.setTotalPages(dataPage.getTotalPages());
		pagination.setDatas(dataPage.toList());
		
		return ResponseEntity.ok(pagination);
	}
	
	/**
	 * Controller xem role
	 * @param page
	 * @return
	 */
	@ApiOperation(value = "Xem tất cả role")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại")
	})
	@GetMapping("role")
	ResponseEntity<Pagination<RoleInfoDto>> viewRole(@RequestParam(defaultValue = "1") int page) {
		Pagination<RoleInfoDto> pagination = new Pagination<>();
		Page<RoleInfoDto> dtoPage = roleInfoService.findAll(page, 5);
		
		pagination.setTotalElements(dtoPage.getTotalElements());
		pagination.setTotalPages(dtoPage.getTotalPages());
		pagination.setDatas(dtoPage.toList());
		return ResponseEntity.ok(pagination);
	}
	
	/**
	 * Tạo mới role
	 * @param form
	 * @return
	 */
	@ApiOperation(value = "Tạo mới role")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại")
	})
	@PostMapping("role")
	ResponseEntity<RoleInfoDto> createRole(@Valid RoleCreateForm form) {
		List<PermissionInfoDto> permissionInfos = new ArrayList<>();
		RoleInfoDto dto = new RoleInfoDto();
		
		// set giá trị cho đối tượng dto
		List<String> permissionsForm = form.getPermissions();
		if(!StringUtil.isNull(permissionsForm) && permissionsForm.size() != 0) {
			permissionInfos = permissionInfoService.findBySlugIn(permissionsForm);
			dto.setPermissions(permissionInfos);
		}
		dto.setName(form.getName());
		
		// tạo mới role
		roleInfoService.create(dto);
		
		return ResponseEntity.ok(dto);
	}
	
	/**
	 * Xóa role
	 * @param roleId
	 * @return
	 */
	@ApiOperation(value = "Xóa role")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại")
	})
	@DeleteMapping("role")
	ResponseEntity<String> deleteRole(@RequestParam(required = true) int roleId) {
		roleInfoService.delete(roleId);
		return ResponseEntity.ok("Thành công");
	}
}
