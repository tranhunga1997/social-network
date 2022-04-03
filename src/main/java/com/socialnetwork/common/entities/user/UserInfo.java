package com.socialnetwork.common.entities.user;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.socialnetwork.common.entities.BaseEntity;
import com.socialnetwork.common.types.BooleanConvert;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name="M_USER_INFO")
@Data
@EqualsAndHashCode(callSuper = false)
public class UserInfo extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1261742782485421495L;
	
	@Id
	@Column(name="user_id")
	private Long userId;
	
	@Column(length = 20, unique = true)
	private String username;
	
	@Column(name="last_name",length = 20)
	private String lastName;
	
	@Column(name="first_name",length = 20)
	private String firstName;
	
	@Column(length = 40, nullable = false)
	private String email;

	@Column(name="is_enabled")
	@Convert(converter = BooleanConvert.class)
	private boolean isEnabled;
	
	@Column(name="is_blocked")
	@Convert(converter = BooleanConvert.class)
	private boolean isBlocked;
	
	// relationship
	@ManyToMany(mappedBy = "users")
	private List<RoleInfo> roles;
}