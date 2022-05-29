package com.socialnetwork.common.entities.user;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.socialnetwork.common.entities.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 
 * @author hung
 *
 */
@Entity
@Table(name = "m_role_info")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = "users")
public class RoleInfo extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1788649198287500155L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="role_id")
	private Integer id;
	@Column(name="role_slug", unique = true, length = 150)
	private String slug;
	@Column(name="role_name", length = 100)
	private String name;
	
//	@ManyToMany(fetch = FetchType.LAZY,targetEntity = UserInfo.class)
//	@JoinTable(name = "m_user_role_link",
//			joinColumns = @JoinColumn(name = "role_id"),
//			inverseJoinColumns = @JoinColumn(name = "user_id"))
//	private List<UserInfo> users;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "roles")
	private List<UserInfo> users;
	
	@ManyToMany(targetEntity = PermissionInfo.class)
	@JoinTable(name = "m_role_permission_link",
			joinColumns = @JoinColumn(name = "role_id"),
			inverseJoinColumns = @JoinColumn(name = "permission_id"))
	private List<PermissionInfo> permissions;

//	@Override
//	public String toString() {
//		return "RoleInfo [id=" + id + ", slug=" + slug + ", name=" + name + ", permissions=" + permissions + "]";
//	}
	
	
}
