package com.socialnetwork.common.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Data;

@MappedSuperclass
@Data
public class BaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1876453224787306485L;
	@Column(name="create_at")
	private LocalDateTime createAt;
	@Column(name="update_at")
	private LocalDateTime updateAt;
	@Column(name="del_at")
	private LocalDateTime deleteAt;
}
