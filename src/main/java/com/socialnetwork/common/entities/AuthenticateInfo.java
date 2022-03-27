package com.socialnetwork.common.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "M_AUTHENTICATE_INFO")
@Data
public class AuthenticateInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2956338042471354684L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "user_id")
	private Long userId;
	private Integer historyId;
	@Column(length = 32)
	private String password;
	private Integer loginFailedCounter;
	@Column(name="create_at")
	private LocalDateTime createDatetime;
	@Column(name="update_at")
	private LocalDateTime updateDatetime;
	@Column(name="del_at")
	private LocalDateTime deleteDatetime;
	/* Relationship */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", updatable = false, insertable = false)
	private UserInfo user;
}
