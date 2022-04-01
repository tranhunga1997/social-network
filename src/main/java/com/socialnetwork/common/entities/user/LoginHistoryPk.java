package com.socialnetwork.common.entities.user;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class LoginHistoryPk implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1262357003437275943L;
	
	private String ipAddress;
	private LocalDateTime accessAt;
}
