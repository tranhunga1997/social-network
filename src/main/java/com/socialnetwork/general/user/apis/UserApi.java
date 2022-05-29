package com.socialnetwork.general.user.apis;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
import org.springframework.util.StringUtils;
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
import com.socialnetwork.general.user.search.PermissionSearch;
import com.socialnetwork.general.user.search.RoleSearch;
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

	/*
	 * ============================================================================================
	 */
	@ApiOperation(value = "Đăng ký tài khoản")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại") })
	@PostMapping("/register")
	ResponseEntity<?> create(HttpServletRequest req, @Valid UserRegisterForm form) {
		// kiểm tra xác nhận mật khẩu
		if (!Objects.equals(form.getPassword(), form.getPasswordConfirm())) {
			throw new InputException("xác nhận mật khẩu", "E_00012", "mật khẩu", "xác nhận mật khẩu");
		}
		// kiểm tra tài khoản tồn tại
		boolean checkExists = userService.isExists(form.getEmail());
		if (checkExists) {
			throw new SocialException("W_00011", "Tạo tài khoản");
		}
		// lấy thông tin role user
		List<RoleInfo> roleInfos = new ArrayList<>();
		RoleDetailInfoDto roleInfoDto = roleInfoService.findById(1); // lấy thông tin role user
		if (StringUtil.isNull(roleInfoDto)) {
			throw new SocialException("E_00003");
		}
		roleInfos.add(roleInfoDto.toRoleInfo());

		// tạo tài khoản
		UserDetailInfoDto userInfoDto = form.toUserInfoDto();
		userInfoDto.setRoles(roleInfos);
		userService.create(userInfoDto);

		// tạo mật khẩu
		authService.create(userInfoDto.getUserId(), form.getPassword());

		// tạo token
		long registTokenExpiryDays = 1L;
		RegistTokenInfoDto registTokenInfoDto = new RegistTokenInfoDto(userInfoDto.getUserId(),
				TokenProvider.generateToken(), registTokenExpiryDays);
		String token = registTokenService.create(registTokenInfoDto).getToken();

		// gửi mail
		String clientHost = req.getLocalName();
		MailUtil.sendTextMail(form.getEmail(), "Kích hoạt tài khoản",
				clientHost + "/api/user/register-active?token=" + token);

		return ResponseEntity.ok().build();
	}

	@ApiOperation(value = "Đăng nhập tài khoản")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại") })
	@PostMapping("/login")
	ResponseEntity<LoginResponseData> login(HttpServletRequest req, @Valid UserLoginForm form) {
		String ipAddress = req.getRemoteAddr();

		// kiểm tra tài khoản
		String username = form.getUsername();
		String password = form.getPassword();
		UserDetailInfoDto userInfoDto = userService.findByUsername(username);
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

	@ApiOperation(value = "Đăng xuất tài khoản")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại") })
	@PostMapping("/logout")
	ResponseEntity<?> logout(HttpServletRequest request) {
		long refreshId = TokenProvider.getRefreshTokenIdFromRequest(request);
		loginTokenInfoService.delete(refreshId);

		return ResponseEntity.ok().build();
	}

	@ApiOperation(value = "Xem thông tin chi tiết")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại") })
	@GetMapping("/{username}")
	ResponseEntity<UserDetailDto> viewDetail(HttpServletRequest request, @PathVariable String username) {
		String usernameLocal = TokenProvider.getUserUsernameFromRequest(request);

		// Kiểm tra thông tin tài khoản muốn lấy có hợp lệ
		if (!username.equals(usernameLocal)) {
			log.debug("Lấy dữ liệu tài khoản " + username + " thất bại");
			throw new SocialException("W_00011", "lấy dữ liệu");
		}

		// lấy dữ liệu tài khoản
		UserDetailInfoDto userInfoDto = userService.findByUsername(usernameLocal);

		// khởi tạo dữ liệu cần thiết trả về
		UserDetailDto userDetailDto = UserDetailDto.builder().firstName(userInfoDto.getFirstName())
				.lastName(userInfoDto.getLastName()).email(userInfoDto.getEmail()).build();
		return ResponseEntity.ok(userDetailDto);
	}

	@ApiOperation(value = "Sửa thông tin chi tiết")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại") })
	@PutMapping("/{username}")
	ResponseEntity<UserDetailDto> updateDetail(HttpServletRequest request, @PathVariable String username,
			UserInfoUpdateForm form) {
		String usernameLocal = TokenProvider.getUserUsernameFromRequest(request);

		// Kiểm tra thông tin tài khoản muốn lấy có hợp lệ
		if (!username.equals(usernameLocal)) {
			log.debug("Lấy dữ liệu tài khoản " + username + " thất bại");
			throw new SocialException("W_00011", "lấy dữ liệu");
		}

		// lấy thông tin user
		UserDetailInfoDto userDto = userService.findByUsername(username);

		userDto.setEmail(form.getEmail());
		userDto.setFirstName(form.getFirstName());
		userDto.setLastName(form.getLastName());
		userDto.setUpdateDatetime(LocalDateTime.now());

		userService.create(userDto);

		UserDetailDto resultDto = UserDetailDto.builder().email(userDto.getEmail()).firstName(userDto.getFirstName())
				.lastName(userDto.getLastName()).build();

		return ResponseEntity.ok(resultDto);
	}

	@ApiOperation(value = "Thay đổi mật khẩu")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại hoặc danh sách lỗi") })
	@PostMapping("/password/change")
	ResponseEntity<String> changePassword(HttpServletRequest request, ChangePasswordForm form) {
		String username = TokenProvider.getUserUsernameFromRequest(request);
		UserDetailInfoDto userInfoDto = userService.findByUsername(username);

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
		MailUtil.sendTextMail(userInfoDto.getEmail(), "Thông báo thay đổi mật khẩu",
				"Mật khẩu của tài khoản (" + username + ") đã được thay đổi.");

		return ResponseEntity.ok("Thành công");
	}

	@ApiOperation(value = "Quên mật khẩu (nhập email)")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại") })
	@PostMapping("/forget-password")
	ResponseEntity<String> forgetPasswordEmailInput(HttpServletRequest request, String email) {
		UserDetailInfoDto userInfoDto = userService.findByEmail(email);
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

	@ApiOperation(value = "Quên mật khẩu (nhập mật khẩu)")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại") })
	@PutMapping("/forget-password")
	ResponseEntity<String> forgetPassword(HttpServletRequest request, @Valid ForgetPasswordForm form) throws Exception {
		if(!StringUtils.hasText(form.getToken())) {
			throw new Exception("Không tìm thấy token quên mật khẩu");
		}
		
		// kiểm tra xác nhận mật khẩu mới
		if (!Objects.equals(form.getNewPassword(), form.getNewPasswordConfirm())) {
			throw new InputException("xác nhận mật khẩu mới", "W_00012", "mật khẩu mới", "xác nhận mật khẩu mới");
		}

		// kiểm tra token
		ForgetPwdTokenInfoDto forgetPwdTokenInfoDto = forgetPwdTokenInfoService.find(form.getToken());

		if (forgetPwdTokenInfoDto.getTokenExpiredAt().isBefore(LocalDateTime.now())) {
			// kiểm tra hiệu lực
			throw new SocialException("W_00021", "token");
		}

		// thay đổi mật khẩu
		authService.update(forgetPwdTokenInfoDto.getUserId(), form.getNewPassword());
		// xóa token (dự định không xóa)
		return ResponseEntity.ok("Thành công");
	}

	@ApiOperation(value = "tìm kiếm thông tin user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại") })
	@GetMapping("/filter")
	ResponseEntity<Pagination<UserDetailInfoDto>> userFilter(UserSearchForm form,
			@RequestParam(defaultValue = "1") int page) {
		UserSearch userSearch = new UserSearch();
		userSearch.setUsername(form.getUsername());
		userSearch.setRoleId(form.getRoleId());
		Page<UserDetailInfoDto> userInfoPage = userService.find(userSearch, PageRequest.of(page - 1, 10));
		return ResponseEntity.ok(
				new Pagination<>(userInfoPage.getTotalPages(), userInfoPage.getTotalElements(), userInfoPage.toList()));
	}

	// TODO ADMIN CONROLLER

	@ApiOperation(value = "Tìm kiếm permission (quyền hạn)")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại") })
	@GetMapping("/permission")
	ResponseEntity<Pagination<PermissionInfoDto>> permissionFilter(HttpServletRequest request, PermissionSearch search,
			@RequestParam(defaultValue = "1") int page) throws Exception {
		// filter
		checkAuthorization("permission-view", TokenProvider.getUserUsernameFromRequest(request));

		Pagination<PermissionInfoDto> pagination = new Pagination<>();
		Page<PermissionInfoDto> dataPage = permissionInfoService.find(search, PageRequest.of(page-1, 10));

		pagination.setTotalElements(dataPage.getTotalElements());
		pagination.setTotalPages(dataPage.getTotalPages());
		pagination.setDatas(dataPage.toList());

		return ResponseEntity.ok(pagination);
	}

	@ApiOperation(value = "Tìm kiếm role")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại") })
	@GetMapping("/role")
	ResponseEntity<Pagination<RoleInfoDto>> roleFilter(HttpServletRequest request, RoleSearch roleSearch,
			@RequestParam(defaultValue = "1") int page) throws Exception {
		// filter
		checkAuthorization("role-search", TokenProvider.getUserUsernameFromRequest(request));

		Pagination<RoleInfoDto> pagination = new Pagination<>();
		Page<RoleDetailInfoDto> dtoPage = roleInfoService.find(roleSearch, PageRequest.of(page-1, 10));

		List<RoleInfoDto> resultDto = new ArrayList<RoleInfoDto>();
		dtoPage.toList().forEach(dto -> {
			resultDto.add(new RoleInfoDto(dto));
		});

		pagination.setTotalElements(dtoPage.getTotalElements());
		pagination.setTotalPages(dtoPage.getTotalPages());
		pagination.setDatas(resultDto);
		return ResponseEntity.ok(pagination);
	}

	@ApiOperation(value = "Xem chi tiết role")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại") })
	@GetMapping("/role/{slug}")
	ResponseEntity<RoleDetailInfoDto> viewDetailRole(HttpServletRequest request, @PathVariable String slug)
			throws Exception {
		// filter
		checkAuthorization("role-search", TokenProvider.getUserUsernameFromRequest(request));

		RoleDetailInfoDto roleInfoDto = roleInfoService.findBySlug(slug);
		roleInfoDto.setPermissions(roleInfoDto.getPermissions());
		return ResponseEntity.ok(roleInfoDto);
	}

	@ApiOperation(value = "Tạo mới role")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại") })
	@PostMapping("/role")
	ResponseEntity<RoleDetailInfoDto> createRole(HttpServletRequest request, @Valid RoleCreateForm form)
			throws Exception {
		// filter
		checkAuthorization("role-add", TokenProvider.getUserUsernameFromRequest(request));

		RoleDetailInfoDto dto = new RoleDetailInfoDto();

		// set giá trị cho đối tượng dto
		List<Integer> permissionIdsForm = form.getPermissionIds();
		if (!StringUtil.isNull(permissionIdsForm) && permissionIdsForm.size() != 0) {
			List<PermissionInfo> permissionInfos = new ArrayList<>();
			permissionInfos = permissionInfoService.findAllByIds(permissionIdsForm);
			// chuyển permission dto sang entity
			dto.setPermissions(permissionInfos);
		}
		dto.setName(form.getName());

		// tạo mới role
		roleInfoService.create(dto);

		return ResponseEntity.ok(dto);
	}

	@ApiOperation(value = "Sửa thông tin role")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại") })
	@PutMapping("/role")
	ResponseEntity<?> updateRole(HttpServletRequest request, @RequestParam int roleId,
			@ApiParam(value = "Ví dụ:\ndanh sách permission id hiện tại: 1,2,3.\nmuốn xóa 2 và thêm 4, dữ liệu gửi như bên dưới\npermissionIds = {1,3,4}") @RequestParam Integer[] perssionIds)
			throws Exception {
		// filter
		checkAuthorization("role-edit", TokenProvider.getUserUsernameFromRequest(request));

		RoleDetailInfoDto roleInfoDto = roleInfoService.findById(roleId);
		if (StringUtil.isNull(roleInfoDto)) {
			throw new SocialException("E_00003");
		}

		// lấy danh sách permission
		roleInfoDto.setPermissions(permissionInfoService.findAllByIds(Arrays.asList(perssionIds)));

		// sửa role
		roleInfoService.update(roleInfoDto);
		return ResponseEntity.ok("Sửa role thành công");
	}

	@ApiOperation(value = "Xóa role")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại") })
	@DeleteMapping("/role")
	ResponseEntity<String> deleteRole(HttpServletRequest request, @RequestParam(required = true) int roleId)
			throws Exception {
		// filter
		checkAuthorization("role-delete", TokenProvider.getUserUsernameFromRequest(request));

		roleInfoService.deleteById(roleId);
		return ResponseEntity.ok("Thành công");
	}

	@ApiOperation(value = "Tính năng khóa tài khoản", notes = "trạng thái tài khoản khóa -> mở khóa \ntrạng thái tài khoản không khóa -> khóa")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại") })
	@PostMapping("/block")
	ResponseEntity<?> blockAndUnblock(HttpServletRequest request, @RequestParam String username) throws Exception {
		// filter
		checkAuthorization("page-block-change", TokenProvider.getUserUsernameFromRequest(request));

		boolean status = userService.findByUsername(username).isBlocked();
		userService.blockAndUnblock(username, status);
		return ResponseEntity.ok().build();
	}

	// TODO private method

	/**
	 * kiểm tra permission theo controller
	 * 
	 * @param permissionId permission cố định theo từng controller
	 * @param username     tên tài khoản
	 * @throws Exception
	 */
	private void checkAuthorization(String permissionSlug, String username) throws Exception {
		Set<String> permissionSlugs = new HashSet<>();
		List<RoleInfo> roles = userService.findByUsername(username).getRoles();
		roles.forEach(r -> {
			r.getPermissions().forEach(p -> {
				permissionSlugs.add(p.getSlug());
			});
		});

		if (!permissionSlugs.contains(permissionSlug)) {
			throw new Exception("Không thể truy cập.");
		}
	}

}
