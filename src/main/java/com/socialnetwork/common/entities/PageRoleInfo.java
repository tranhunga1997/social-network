package com.socialnetwork.common.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
/**
 * Lưu thông tin quyền hạn trong page
 * owner, admin, staff,  member...
 * @author thuong
 *
 */
@Entity
@Table(name="m_page_role_info")
@Data
public class PageRoleInfo {
	@Id
	@Column(name="page_role_id")
	private Long pageRoleId;
	@Column(name="page_role_name", length = 255, nullable = false)
	private String pageRolename;
}
