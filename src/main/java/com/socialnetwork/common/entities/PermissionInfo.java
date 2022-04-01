package com.socialnetwork.common.entities;

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

import lombok.Data;

@Entity
@Table(name = "M_PERMISSION_INFO")
@Data
public class PermissionInfo extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2285148575888297526L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 20, nullable = false, unique = true)
	private String slug;
	@Column(length = 20, nullable = false)
	private String name;
	
	@ManyToMany(targetEntity = RoleInfo.class)
	@JoinTable(name = "ROLES_PERMISSIONS_LINK",
			joinColumns = @JoinColumn(name = "permission_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<RoleInfo> roles;
}
