package com.socialnetwork.common.entities.user;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.socialnetwork.common.entities.BaseEntity;

import lombok.Data;

@Entity
@Table(name = "M_ROLE_INFO")
@Data
public class RoleInfoEntity extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1788649198287500155L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 32)
	private String slug;
	@Column(length = 32)
	private String name;
	
	@ManyToMany(targetEntity = UserEntity.class)
	@JoinTable(name = "USERS_ROLES_LINK",
			joinColumns = @JoinColumn(name = "role_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<UserEntity> users;
	
	@ManyToMany(mappedBy = "roles")
	private List<PermissionInfoEntity> permissions;
}
