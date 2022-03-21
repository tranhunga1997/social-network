package com.socialnetwork.common.entities.user;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.socialnetwork.common.entities.BaseEntity;
import com.socialnetwork.common.entities.user.pk.LoginHistioryPK;

import lombok.Data;

@Entity
@Table(name = "H_LOGIN_INFO")
@Data
@IdClass(LoginHistioryPK.class)
public class LoginHistoryEntity extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6308675124624326206L;
	@Id
	private String ipAddress;
	@Id
	private LocalDateTime accessDatetime;
	@Column(nullable = false)
	private Long userId;

}
