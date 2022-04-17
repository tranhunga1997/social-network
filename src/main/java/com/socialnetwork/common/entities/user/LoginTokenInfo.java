package com.socialnetwork.common.entities.user;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
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
@Table(name = "T_LOGIN_TOKEN_INFO")
@Data
@EqualsAndHashCode(callSuper = false)
public class LoginTokenInfo extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="user_id", nullable = false)
	private Long userId;
	
	@Column(length = 50, nullable = false)
	private String token;
	
	@Column(name="ip_address",length = 20, nullable = false)
	private String ipAddress;

	@Column(name="token_expired_at",nullable = false)
	private LocalDateTime tokenExpiredAt;
}
