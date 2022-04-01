package com.socialnetwork.common.entities.user;

import java.io.Serializable;

import lombok.Data;

@Data
public class AuthenticatePk implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long userId;
	private Integer historyId;
}
