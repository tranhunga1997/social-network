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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.socialnetwork.common.entities.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 
 * @author hung
 *
 */
@Entity
@Table(name = "m_permission_info")
@Data
@EqualsAndHashCode(callSuper = false)
public class PermissionInfo extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2285148575888297526L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="permission_id")
	private Integer id;
	@Column(name="permission_slug",length = 150, nullable = false, unique = true)
	private String slug;
	@Column(name= "permission_name",length = 100, nullable = false)
	private String name;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "permissions")
	private List<RoleInfo> roles;

	@Override
	public String toString() {
		return "PermissionInfo [id=" + id + ", slug=" + slug + ", name=" + name + "]";
	}
	
	
}
