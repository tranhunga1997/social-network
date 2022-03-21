package com.socialnetwork.common.entities.user.pk;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class LoginHistioryPK implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1262357003437275943L;
	
	private String ipAddress;
	private LocalDateTime accessDatetime;
}
