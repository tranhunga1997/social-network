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
	private Long userId;
	@Column(length = 50, nullable = false)
	private String token;
	@Column(nullable = false)
	private LocalDateTime activeAt;

}
