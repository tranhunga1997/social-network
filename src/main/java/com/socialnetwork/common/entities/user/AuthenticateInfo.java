package com.socialnetwork.common.entities.user;

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

@Entity
@Table(name = "M_AUTHENTICATE_INFO")
@Data
@EqualsAndHashCode(callSuper = false)
@IdClass(AuthenticatePk.class)
public class AuthenticateInfo extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2956338042471354684L;
	@Id
	@Column(name="user_id")
	private Long userId;
	
	@Id
	@Column(name="history_id")
	private Integer historyId;
	
	@Column(length = 255)
	private String password;
	
	@Column(name="login_failed_counter")
	private Integer loginFailedCounter;
	
	// relationship
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private UserInfo user;
}
