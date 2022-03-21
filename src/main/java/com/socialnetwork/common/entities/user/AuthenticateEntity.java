package com.socialnetwork.common.entities.user;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.socialnetwork.common.entities.BaseEntity;

import lombok.Data;

@Entity
@Table(name = "M_AUTHENTICATE_INFO")
@Data
public class AuthenticateEntity extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2956338042471354684L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;
	private Integer historyId;
	@Column(length = 32)
	private String password;
	private Integer loginFailedCounter;
}
