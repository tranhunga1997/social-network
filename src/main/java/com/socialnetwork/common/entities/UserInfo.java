package com.socialnetwork.common.entities;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="M_USER_INFO")
@Data
public class UserInfo extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1261742782485421495L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(length = 50, unique = true)
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
	private LocalDateTime createDatetime;
	private LocalDateTime updateDatetime;
	private LocalDateTime deleteDatetime;
}