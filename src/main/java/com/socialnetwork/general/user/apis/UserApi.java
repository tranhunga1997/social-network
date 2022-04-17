package com.socialnetwork.general.user.apis;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.socialnetwork.common.entities.user.RoleInfo;
import com.socialnetwork.common.exceptions.InputException;
import com.socialnetwork.common.exceptions.SocialException;
import com.socialnetwork.common.utils.MailService;
import com.socialnetwork.common.utils.StringUtil;
import com.socialnetwork.common.utils.TokenProvider;
import com.socialnetwork.general.user.dtos.AuthenticateInfoDto;
import com.socialnetwork.general.user.dtos.LoginHistoryInfoDto;
import com.socialnetwork.general.user.dtos.LoginResponseData;
import com.socialnetwork.general.user.dtos.LoginTokenInfoDto;
import com.socialnetwork.general.user.dtos.RegistTokenInfoDto;
import com.socialnetwork.general.user.dtos.RoleInfoDto;
import com.socialnetwork.general.user.dtos.UserInfoDto;
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
	private UserService userService;
	@Autowired
	private AuthenticateService authService;
	@Autowired
	private RoleInfoService roleInfoService;
	@Autowired
	private RegistTokenService registTokenService;
	@Autowired
	private LoginTokenInfoService loginTokenInfoService;
	@Autowired
	private MailService mailService;
	@Autowired
	private LoginHistoryService loginHistoryService;
	
	/* ============================================================================================ */
	
	@GetMapping("/test-logger")
	String testLogger() {
		log.debug("test logger!!!");
		log.info("test logger!!!");
		log.warn("test logger!!!");
		return "test logger";
	}
	
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
	ResponseEntity<?> create(HttpServletRequest req, @Validated UserRegisterForm form){
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
		mailService.sendTextMail(form.getEmail(), "Kích hoạt tài khoản", clientHost+"/api/user/register-active?token="+token);
		
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
	ResponseEntity<LoginResponseData> login(HttpServletRequest req, @Validated UserLoginForm form){
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
		String jwt = TokenProvider.generateJwt(username, userInfoDto.getUserId());
		
		// ghi lại lịch sử đăng nhập
		LoginHistoryInfoDto loginHistoryInfoDto = new LoginHistoryInfoDto();
		loginHistoryInfoDto.setAccessAt(LocalDateTime.now());
		loginHistoryInfoDto.setIpAddress(ipAddress);
		loginHistoryInfoDto.setUserId(userInfoDto.getUserId());
		loginHistoryService.create(loginHistoryInfoDto);
		
		// trả về jwt và refresh token
		return ResponseEntity.ok(new LoginResponseData(jwt, loginTokenInfoDto.getRefreshToken()));
	}
}
