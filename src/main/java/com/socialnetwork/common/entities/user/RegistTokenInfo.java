package com.socialnetwork.common.entities.user;

import java.io.Serializable;
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
@Table(name = "T_REGIST_TOKEN_INFO")
@Data
@EqualsAndHashCode(callSuper = false)
public class RegistTokenInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -162074673424250813L;
	
	@Id
	@Column(name="user_id", nullable = false)
	private Long userId;
	
	@Column(length = 100, nullable = false)
	private String token;
	
	@Column(name="token_expired_at",nullable = false)
	private LocalDateTime tokenExpiredAt;
	
	@Column(name = "create_at", nullable = false)
	private LocalDateTime createAt;
	
	@Column(name = "update_at")
	private LocalDateTime updateAt;
}
