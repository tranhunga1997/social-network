package com.socialnetwork.common.entities.user;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.socialnetwork.common.entities.BaseEntity;

import lombok.Data;

@Entity
@Table(name = "T_LOGIN_TOKEN_INFO")
@Data
public class LoginTokenEntity extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6844799339149998835L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 12, nullable = false)
	private String ipAddress;
	private Long userId;
	@Column(length = 100, nullable = false)
	private String token;
	@Column(nullable = false)
	private LocalDate tokenExpiredDate;

}
