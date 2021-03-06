package com.socialnetwork.general.user.apis;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.socialnetwork.general.user.dtos.RoleDetailInfoDto;
import com.socialnetwork.general.user.dtos.UserDetailDto;
import com.socialnetwork.general.user.dtos.UserDetailInfoDto;
import com.socialnetwork.general.user.dtos.UserInfoDto;
import com.socialnetwork.general.user.forms.ChangePasswordForm;
import com.socialnetwork.general.user.forms.ForgetPasswordForm;
import com.socialnetwork.general.user.forms.RoleCreateForm;
import com.socialnetwork.general.user.forms.UserInfoUpdateForm;
import com.socialnetwork.general.user.forms.UserLoginForm;
import com.socialnetwork.general.user.forms.UserRegisterForm;
import com.socialnetwork.general.user.forms.UserSearchForm;
import com.socialnetwork.general.user.search.UserSearch;
import com.socialnetwork.general.user.services.impl.AuthenticateService;
import com.socialnetwork.general.user.services.impl.ForgetPwdTokenInfoService;
import com.socialnetwork.general.user.services.impl.LoginHistoryService;
import com.socialnetwork.general.user.services.impl.LoginTokenInfoService;
import com.socialnetwork.general.user.services.impl.PermissionInfoService;
import com.socialnetwork.general.user.services.impl.RegistTokenService;
import com.socialnetwork.general.user.services.impl.RoleInfoDto;
import com.socialnetwork.general.user.services.impl.RoleInfoService;
import com.socialnetwork.general.user.services.impl.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
	@ApiOperation(value = "????ng k?? t??i kho???n")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Th??nh c??ng"),
			@ApiResponse(code = 400, message = "Th???t b???i")
	})
	@PostMapping("/register")
	ResponseEntity<?> create(HttpServletRequest req, @Valid UserRegisterForm form){
		// ki???m tra x??c nh???n m???t kh???u
		if(!Objects.equals(form.getPassword(), form.getPasswordConfirm())) {
			throw new InputException("x??c nh???n m???t kh???u", "E_00012", "m???t kh???u", "x??c nh???n m???t kh???u");
		}
		// ki???m tra t??i kho???n t???n t???i
		boolean checkExists = userService.isExists(form.getEmail());
		if(checkExists) {
			throw new SocialException("W_00011", "T???o t??i kho???n");
		}
		//l???y th??ng tin role user
		List<RoleInfo> roleInfos = new ArrayList<>(); 
		RoleDetailInfoDto roleInfoDto = roleInfoService.findById(1); // l???y th??ng tin role user
		if(StringUtil.isNull(roleInfoDto)) {
			throw new SocialException("E_00003");
		}
		roleInfos.add(roleInfoDto.toRoleInfo());
		
		// t???o t??i kho???n
		UserDetailInfoDto userInfoDto = form.toUserInfoDto();
		userInfoDto.setRoles(roleInfos);
		userService.create(userInfoDto);
		
		// t???o m???t kh???u
		authService.create(userInfoDto.getUserId(), form.getPassword());
		
		// t???o token
		long registTokenExpiryDays = 1L;
		RegistTokenInfoDto registTokenInfoDto = new RegistTokenInfoDto(userInfoDto.getUserId(), TokenProvider.generateToken(), registTokenExpiryDays);
		String token = registTokenService.create(registTokenInfoDto).getToken();
		
		// g???i mail
		String clientHost = req.getLocalName();
		MailUtil.sendTextMail(form.getEmail(), "K??ch ho???t t??i kho???n", clientHost+"/api/user/register-active?token="+token);
		
		return ResponseEntity.ok().build();
	}

	@ApiOperation(value = "????ng nh???p t??i kho???n")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Th??nh c??ng"),
			@ApiResponse(code = 400, message = "Th???t b???i")
	})
	@PostMapping("/login")
	ResponseEntity<LoginResponseData> login(HttpServletRequest req, @Valid UserLoginForm form){
		String ipAddress = req.getRemoteAddr();
		
		// ki???m tra t??i kho???n
		String username = form.getUsername();
		String password = form.getPassword();
		UserDetailInfoDto userInfoDto = userService.findByUsername(username);
		AuthenticateInfoDto authenticateInfoDto = authService.findNewInfo(userInfoDto.getUserId());
		
		// x??c th???c t??i kho???n
		int authCode = authService.authentication(userInfoDto, authenticateInfoDto, password);
		switch (authCode) {
		case -1:
			// t??i kho???n kh??ng t???n t???i
			throw new SocialException("W_00011", "????ng nh???p"); 
		case 0: 
			// th??nh c??ng
			break;
		case 1:
			 // t??i kho???n ch??a k??ch ho???t
			throw new SocialException("W_00014");
		case 2:
			// t??i kho???n b??? kh??a
			throw new SocialException("W_00015"); 
		case 3:
			// qu?? s??? l???n ????ng nh???p
			userService.blockAndUnblock(username, false);
			throw new SocialException("W_00013"); 
		case 4:
			// sai m???t kh???u
			authService.loginFailedAction(userInfoDto.getUserId(), authenticateInfoDto.getLoginFailedCounter());
			throw new SocialException("W_00011", "????ng nh???p"); 
		}
		
		// kh???i t???o jwt v?? refresh token
		LoginTokenInfoDto loginTokenInfoDto = loginTokenInfoService.create(userInfoDto.getUserId(), ipAddress);
		String jwt = TokenProvider.generateJwt(username, loginTokenInfoDto.getRefreshId());
		
		// ghi l???i l???ch s??? ????ng nh???p
		LoginHistoryInfoDto loginHistoryInfoDto = new LoginHistoryInfoDto();
		loginHistoryInfoDto.setAccessAt(LocalDateTime.now());
		loginHistoryInfoDto.setIpAddress(ipAddress);
		loginHistoryInfoDto.setUserId(userInfoDto.getUserId());
		loginHistoryService.create(loginHistoryInfoDto);
		
		// tr??? v??? jwt v?? refresh token
		return ResponseEntity.ok(new LoginResponseData(jwt, loginTokenInfoDto.getRefreshToken()));
	}

	@ApiOperation(value = "????ng xu???t t??i kho???n")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Th??nh c??ng"),
			@ApiResponse(code = 400, message = "Th???t b???i")
	})
	@PostMapping("/logout")
	ResponseEntity<?> logout(HttpServletRequest request){
		long refreshId = TokenProvider.getRefreshTokenIdFromRequest(request);
		loginTokenInfoService.delete(refreshId);
		
		return ResponseEntity.ok().build();
	}

	@ApiOperation(value = "Xem th??ng tin chi ti???t")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Th??nh c??ng"),
			@ApiResponse(code = 400, message = "Th???t b???i")
	})
	@GetMapping("/{username}")
	ResponseEntity<UserDetailDto> viewDetail(HttpServletRequest request, @PathVariable String username) {
		String usernameLocal = TokenProvider.getUserUsernameFromRequest(request);
		
		// Ki???m tra th??ng tin t??i kho???n mu???n l???y c?? h???p l???
		if(!username.equals(usernameLocal)) {
			log.debug("L???y d??? li???u t??i kho???n " + username + " th???t b???i");
			throw new SocialException("W_00011", "l???y d??? li???u");
		}
		
		// l???y d??? li???u t??i kho???n
		UserDetailInfoDto userInfoDto = userService.findByUsername(usernameLocal);
		
		// kh???i t???o d??? li???u c???n thi???t tr??? v???
		UserDetailDto userDetailDto = UserDetailDto.builder()
				.firstName(userInfoDto.getFirstName())
				.lastName(userInfoDto.getLastName())
				.email(userInfoDto.getEmail())
				.build();
		return ResponseEntity.ok(userDetailDto);
	}

	@ApiOperation(value = "S???a th??ng tin chi ti???t")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Th??nh c??ng"),
			@ApiResponse(code = 400, message = "Th???t b???i")
	})
	@PutMapping("/{username}")
	ResponseEntity<UserDetailDto> updateDetail(HttpServletRequest request, @PathVariable String username, UserInfoUpdateForm form) {
		String usernameLocal = TokenProvider.getUserUsernameFromRequest(request);
		
		// Ki???m tra th??ng tin t??i kho???n mu???n l???y c?? h???p l???
		if(!username.equals(usernameLocal)) {
			log.debug("L???y d??? li???u t??i kho???n " + username + " th???t b???i");
			throw new SocialException("W_00011", "l???y d??? li???u");
		}
		
		// l???y th??ng tin user
		UserDetailInfoDto userDto = userService.findByUsername(username);
		
		userDto.setEmail(form.getEmail());
		userDto.setFirstName(form.getFirstName());
		userDto.setLastName(form.getLastName());
		userDto.setUpdateDatetime(LocalDateTime.now());
		
		userService.create(userDto);
		
		UserDetailDto resultDto = UserDetailDto.builder()
				.email(userDto.getEmail())
				.firstName(userDto.getFirstName())
				.lastName(userDto.getLastName())
				.build();
		
		return ResponseEntity.ok(resultDto);
	}
	
	@ApiOperation(value = "Thay ?????i m???t kh???u")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Th??nh c??ng"),
			@ApiResponse(code = 400, message = "Th???t b???i ho???c danh s??ch l???i")
	})
	@PostMapping("/password/change")
	ResponseEntity<String> changePassword(HttpServletRequest request, ChangePasswordForm form) {
		String username = TokenProvider.getUserUsernameFromRequest(request);
		UserDetailInfoDto userInfoDto = userService.findByUsername(username);
		
		// ki???m tra t???n t???i t??i kho???n
		if (StringUtil.isNull(userInfoDto)) {
			throw new SocialException("E_00003");
		}
		
		// ki???m tra m???t kh???u m???i kh??ng tr??ng m???t kh???u c??
		if (Objects.equals(form.getPassword(), form.getNewPassword())) {
			throw new InputException("m???t kh???u", "W_00019", "M???t kh???u c??", "m???t kh???u m???i");
		}
		
		// ki???m tra x??c nh???n m???t kh???u
		if (!Objects.equals(form.getNewPassword(), form.getNewPasswordConfirm())) {
			throw new InputException("x??c nh???n m???t kh???u", "W_00012", "m???t kh???u", "x??c nh???n m???t kh???u");
		}
		
		// x??? l?? thay ?????i m???t kh???u
		authService.update(userInfoDto.getUserId(), form.getNewPassword());
		
		// g???i email th??ng b??o
		MailUtil.sendTextMail(userInfoDto.getEmail(), "Th??ng b??o thay ?????i m???t kh???u", "M???t kh???u c???a t??i kho???n (" + username + ") ???? ???????c thay ?????i.");
		
		return ResponseEntity.ok("Th??nh c??ng");
	}
	
	@ApiOperation(value = "Qu??n m???t kh???u (nh???p email)")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Th??nh c??ng"),
			@ApiResponse(code = 400, message = "Th???t b???i")
	})
	@PostMapping("/forget-password")
	ResponseEntity<String> forgetPasswordEmailInput(HttpServletRequest request, String email) {
		UserDetailInfoDto userInfoDto = userService.findByEmail(email);
		// ki???m tra email t???n t???i
		if (StringUtil.isNull(userInfoDto)) {
			throw new SocialException("E_00003");
		}
		
		// kh???i t???o token 
		ForgetPwdTokenInfoDto forgetPwdTokenInfoDto = forgetPwdTokenInfoService.create(userInfoDto.getUserId());
		// g???i mail
		MailUtil.sendTextMail(email, "Qu??n m???t kh???u", "M?? x??c nh???n: " + forgetPwdTokenInfoDto.getToken());
		return ResponseEntity.ok("Th??nh c??ng");
	}
	
	@ApiOperation(value = "Qu??n m???t kh???u (nh???p m???t kh???u)")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Th??nh c??ng"),
			@ApiResponse(code = 400, message = "Th???t b???i")
	})
	@PutMapping("/forget-password")
	ResponseEntity<String> forgetPassword(HttpServletRequest request, @Valid ForgetPasswordForm form) {
		// ki???m tra x??c nh???n m???t kh???u m???i
		if(!Objects.equals(form.getNewPassword(), form.getNewPasswordConfirm())) {
			throw new InputException("x??c nh???n m???t kh???u m???i", "W_00012", "m???t kh???u m???i", "x??c nh???n m???t kh???u m???i");
		}
		
		// ki???m tra token
		ForgetPwdTokenInfoDto forgetPwdTokenInfoDto = forgetPwdTokenInfoService.find(form.getToken());
		
		if(forgetPwdTokenInfoDto.getTokenExpiredAt().isBefore(LocalDateTime.now())) {
			// ki???m tra hi???u l???c
			throw new SocialException("W_00021", "token");
		}
		
		// thay ?????i m???t kh???u
		authService.update(forgetPwdTokenInfoDto.getUserId(), form.getNewPassword());
		// x??a token (d??? ?????nh kh??ng x??a)
		return ResponseEntity.ok("Th??nh c??ng");
	}
	
	// TODO ADMIN CONROLLER
	
	@ApiOperation(value = "Xem t???t c??? permission (quy???n h???n)")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Th??nh c??ng"),
			@ApiResponse(code = 400, message = "Th???t b???i")
	})
	@GetMapping("/permission")
	ResponseEntity<Pagination<PermissionInfoDto>> viewPermission(@RequestParam(defaultValue = "1") int page){
		Pagination<PermissionInfoDto> pagination = new Pagination<>();
		Page<PermissionInfoDto> dataPage = permissionInfoService.findAll(page-1, 5);
		
		pagination.setTotalElements(dataPage.getTotalElements());
		pagination.setTotalPages(dataPage.getTotalPages());
		pagination.setDatas(dataPage.toList());
		
		return ResponseEntity.ok(pagination);
	}
	
	@ApiOperation(value = "Xem t???t c??? role")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Th??nh c??ng"),
			@ApiResponse(code = 400, message = "Th???t b???i")
	})
	@GetMapping("/role")
	ResponseEntity<Pagination<RoleInfoDto>> viewRole(@RequestParam(defaultValue = "1") int page) {
		Pagination<RoleInfoDto> pagination = new Pagination<>();
		Page<RoleDetailInfoDto> dtoPage = roleInfoService.findAll(page-1, 5);
		List<RoleInfoDto> resultDto = new ArrayList<RoleInfoDto>();
		dtoPage.toList().forEach(dto -> {
			resultDto.add(new RoleInfoDto(dto));
		});
		
		pagination.setTotalElements(dtoPage.getTotalElements());
		pagination.setTotalPages(dtoPage.getTotalPages());
		pagination.setDatas(resultDto);
		return ResponseEntity.ok(pagination);
	}
	
	@ApiOperation(value = "Xem chi ti???t role")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Th??nh c??ng"),
			@ApiResponse(code = 400, message = "Th???t b???i")
	})
	@GetMapping("/role/{slug}")
	ResponseEntity<RoleDetailInfoDto> viewOneRole(@PathVariable String slug) {
		RoleDetailInfoDto roleInfoDto = roleInfoService.findBySlug(slug);
		roleInfoDto.setPermissions(roleInfoDto.getPermissions());
		return ResponseEntity.ok(roleInfoDto);
	}

	@ApiOperation(value = "T???o m???i role")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Th??nh c??ng"),
			@ApiResponse(code = 400, message = "Th???t b???i")
	})
	@PostMapping("/role")
	ResponseEntity<RoleDetailInfoDto> createRole(@Valid RoleCreateForm form) {
		RoleDetailInfoDto dto = new RoleDetailInfoDto();
		
		// set gi?? tr??? cho ?????i t?????ng dto
		List<Integer> permissionIdsForm = form.getPermissionIds();
		if(!StringUtil.isNull(permissionIdsForm) && permissionIdsForm.size() != 0) {
			List<PermissionInfo> permissionInfos = new ArrayList<>();
			permissionInfos = permissionInfoService.findAllByIds(permissionIdsForm);
			// chuy???n permission dto sang entity
			dto.setPermissions(permissionInfos);
		}
		dto.setName(form.getName());
		
		// t???o m???i role
		roleInfoService.create(dto);
		
		return ResponseEntity.ok(dto);
	}
	
	@ApiOperation(value = "S???a th??ng tin role")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Th??nh c??ng"),
			@ApiResponse(code = 400, message = "Th???t b???i")
	})
	@PutMapping("/role")
	ResponseEntity<?> updateRole(@RequestParam int roleId, @ApiParam(value = "V?? d???:\ndanh s??ch permission id hi???n t???i: 1,2,3.\nmu???n x??a 2 v?? th??m 4, d??? li???u g???i nh?? b??n d?????i\npermissionIds = {1,3,4}") @RequestParam Integer[] perssionIds) {
		
		RoleDetailInfoDto roleInfoDto = roleInfoService.findById(roleId);
		if(StringUtil.isNull(roleInfoDto)) {
			throw new SocialException("E_00003");
		}
		
		
		// l???y danh s??ch permission
		roleInfoDto.setPermissions(permissionInfoService.findAllByIds(Arrays.asList(perssionIds)));
		
		// s???a role
		roleInfoService.update(roleInfoDto);
		return ResponseEntity.ok("S???a role th??nh c??ng");
	}
	
	@ApiOperation(value = "X??a role")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Th??nh c??ng"),
			@ApiResponse(code = 400, message = "Th???t b???i")
	})
	@DeleteMapping("/role")
	ResponseEntity<String> deleteRole(@RequestParam(required = true) int roleId) {
		roleInfoService.deleteById(roleId);
		return ResponseEntity.ok("Th??nh c??ng");
	}
	
	@ApiOperation(value = "xem t???t c??? th??ng tin user")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Th??nh c??ng"),
			@ApiResponse(code = 400, message = "Th???t b???i")
	})
	@GetMapping("")
	ResponseEntity<Pagination<UserInfoDto>> viewAllUser(@RequestParam(defaultValue = "1") int page) {
		Pageable pageable = PageRequest.of(page-1, 10, Sort.by("userId").ascending());
		Page<UserDetailInfoDto> uDetailInfoPage = userService.findAll(pageable);
		// convert page userdetaildto -> page userdto
		List<UserInfoDto> userInfoDtos = new ArrayList<UserInfoDto>();
		uDetailInfoPage.toList().forEach(u -> {
			userInfoDtos.add(new UserInfoDto(u));
		});
		
		return ResponseEntity.ok(new Pagination<>(uDetailInfoPage.getTotalPages(), uDetailInfoPage.getTotalElements(), userInfoDtos));
	}
	
	@ApiOperation(value = "t??m ki???m th??ng tin user")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Th??nh c??ng"),
			@ApiResponse(code = 400, message = "Th???t b???i")
	})
	@GetMapping("/filter")
	ResponseEntity<Pagination<UserDetailInfoDto>> userFilter(UserSearchForm form, @RequestParam(defaultValue = "1") int page) {
		UserSearch userSearch = new UserSearch();
		userSearch.setUsername(form.getUsername());
		userSearch.setRoleId(form.getRoleId());
		Page<UserDetailInfoDto> userInfoPage = userService.find(userSearch, PageRequest.of(page-1, 10));
		return ResponseEntity.ok(new Pagination<>(userInfoPage.getTotalPages(), userInfoPage.getTotalElements(), userInfoPage.toList()));
	}
	
	@ApiOperation(value = "T??nh n??ng kh??a t??i kho???n", notes = "tr???ng th??i t??i kho???n kh??a -> m??? kh??a \ntr???ng th??i t??i kho???n kh??ng kh??a -> kh??a")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Th??nh c??ng"),
			@ApiResponse(code = 400, message = "Th???t b???i")
	})
	@PostMapping("/block")
	ResponseEntity<?> blockAndUnblock(@RequestParam String username) {
		boolean status = userService.findByUsername(username).isBlocked();
		userService.blockAndUnblock(username, status);
		return ResponseEntity.ok().build();
	}
	
}
