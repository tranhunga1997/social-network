package com.socialnetwork.common.entities.user;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "T_REGIST_TOKEN_HISTORY")
@Data
public class RegistTokenHistory implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(length = 100, nullable = false)
	private String token;
	@Column(name="user_id")
	private Long userId;
	@Column(nullable = false, name="active_at")
	private LocalDateTime activeAt;

}
