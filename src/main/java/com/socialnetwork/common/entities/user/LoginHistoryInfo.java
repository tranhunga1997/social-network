package com.socialnetwork.common.entities.user;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "H_LOGIN_INFO")
@Data
@IdClass(LoginHistoryInfoPK.class)
@EqualsAndHashCode(callSuper = false)
public class LoginHistoryInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6308675124624326206L;
	@Id
	@Column(name="ip_address", length = 20)
	private String ipAddress;
	@Id
	@Column(name="access_at")
	private LocalDateTime accessAt;
	
	@Column(nullable = false, name="user_id")
	private Long userId;

	//relationship
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id", insertable = false, updatable = false)
	private UserInfo userInfo;
}
