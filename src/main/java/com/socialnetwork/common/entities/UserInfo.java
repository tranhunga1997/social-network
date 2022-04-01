package com.socialnetwork.common.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

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
	@Column(length = 50, nullable = false)
	private String username;
	@Column(length = 50)
	private String lastName;
	@Column(length = 50)
	private String firstName;
	@Column(length = 100, nullable = false)
	private String email;
	
	@ManyToMany(mappedBy = "users")
	private List<RoleInfo> roles;
	
	private boolean enable;
	private boolean block;
}