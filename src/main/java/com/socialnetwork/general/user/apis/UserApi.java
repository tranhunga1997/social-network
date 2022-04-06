package com.socialnetwork.general.user.apis;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.socialnetwork.common.entities.user.RoleInfo;
import com.socialnetwork.common.exceptions.InputException;
import com.socialnetwork.common.exceptions.SocialException;
import com.socialnetwork.common.utils.MailService;
import com.socialnetwork.common.utils.StringUtil;
import com.socialnetwork.common.utils.TokenProvider;
import com.socialnetwork.general.user.dtos.RegistTokenInfoDto;
import com.socialnetwork.general.user.dtos.RoleInfoDto;
import com.socialnetwork.general.user.dtos.UserInfoDto;
import com.socialnetwork.general.user.forms.UserRegisterForm;
import com.socialnetwork.general.user.services.AuthenticateService;
import com.socialnetwork.general.user.services.RegistTokenService;
import com.socialnetwork.general.user.services.RoleInfoService;
import com.socialnetwork.general.user.services.UserService;

@CrossOrigin("*")
@RestController
@RequestMapping("api/user")
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
	private MailService mailService;
	
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
		RoleInfoDto roleInfoDto = roleInfoService.findById(2); // lấy thông tin role user
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
}
