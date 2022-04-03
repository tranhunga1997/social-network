package com.socialnetwork.general.user.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.socialnetwork.common.entities.ReponseData;
import com.socialnetwork.common.exceptions.SocialException;
import com.socialnetwork.common.utils.StringUtil;
import com.socialnetwork.general.user.dtos.AuthenticateInfoDto;
import com.socialnetwork.general.user.services.AuthenticateService;
import com.socialnetwork.general.user.services.UserService;

@CrossOrigin("*")
@RestController
@RequestMapping("api/user")
public class UserApi {
	@Autowired
	private UserService userService;
	@Autowired
	private AuthenticateService authService;
	
	ResponseEntity<ReponseData> login(String username, String password){
		// kiểm tra null và chuỗi trống
		if(StringUtil.isNull(username) || StringUtil.isNull(password) || StringUtil.isBlank(username) || StringUtil.isBlank(password)) {
			throw new SocialException("W_00002");
		}
		// kiểm tra tồn tại username
		if(!userService.isExists(username)) {
			throw new SocialException("E_00003");
		}
		// kiểm tra password
		return null;
		
	}
}
