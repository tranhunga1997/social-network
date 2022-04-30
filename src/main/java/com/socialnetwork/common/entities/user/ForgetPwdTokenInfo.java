package com.socialnetwork.common.entities.user;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
/**
 * 
 * @author hung
 *
 */
@Entity
@Table(name = "T_FORGET_PASSWORD_TOKEN_INFO")
@Data
@NoArgsConstructor
public class ForgetPwdTokenInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id 
	@Column(name = "TOKEN_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long tokenId;
	
	@Column(name="user_id")
	private Long userId;

	@Column(length = 50, nullable = false)
	private String token;
	
	@Column(name= "token_expired_at", nullable = false)
	private LocalDateTime tokenExpiredAt;
	
	@Column(name = "create_at", nullable = false)
	private LocalDateTime createAt;
	
	@Column(name = "update_at")
	private LocalDateTime updateAt;
}
