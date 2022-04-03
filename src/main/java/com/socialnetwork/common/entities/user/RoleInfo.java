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
import lombok.EqualsAndHashCode;

/**
 * 
 * @author hung
 *
 */
@Entity
@Table(name = "m_role_info")
@Data
@EqualsAndHashCode(callSuper = false)
public class RoleInfo extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1788649198287500155L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="role_id")
	private Integer id;
	@Column(name="role_slug",length = 32)
	private String slug;
	@Column(name="role_name",length = 32)
	private String name;
	
	@ManyToMany(targetEntity = UserInfo.class)
	@JoinTable(name = "m_user_role_link",
			joinColumns = @JoinColumn(name = "role_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<UserInfo> users;
	
	@ManyToMany(mappedBy = "roles")
	private List<PermissionInfo> permissions;
}
