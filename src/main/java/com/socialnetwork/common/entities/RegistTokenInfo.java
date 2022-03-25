package com.socialnetwork.common.entities;

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
@Table(name = "T_REGIST_TOKEN_INFO")
@Data
public class RegistTokenInfo extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -162074673424250813L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long userId;
	@Column(length = 100, nullable = false)
	private String token;
	@Column(nullable = false)
	private LocalDate tokenExpiredDate;
	
}