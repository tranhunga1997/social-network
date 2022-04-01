package com.socialnetwork.common.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.socialnetwork.common.entities.blockcause.BlockCauseInfo;
import com.socialnetwork.common.types.BooleanConvert;

import lombok.Data;

/**
 * Dành cho pageInfo, statusInfo, userinfo, commentLv1Info, commentLv2Info
 * @author thuong
 *
 */
@MappedSuperclass
@Data
public abstract class BaseSocialEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// block_flag
	@Column(name = "block_flag", nullable = false)
	@Convert(converter = BooleanConvert.class)
	private boolean blockFlag;
	// del_flag
	@Column(name = "del_flag", nullable = false)
	@Convert(converter = BooleanConvert.class)
	private boolean delFlag;
	// create_at
	@Column(name = "create_at")
	private LocalDateTime createAt;
	// update_at
	@Column(name = "update_at")
	private LocalDateTime updateAt;
	// del_at
	@Column(name = "del_at")
	private LocalDateTime delAt;
	// block_at
	@Column(name = "block_at")
	private LocalDateTime blockAt;
	// block_cause_id
	@Column(name = "block_cause_id")
	private Integer blockCauseId;
	
	@JoinColumn(name="block_cause_id", updatable = false, insertable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private BlockCauseInfo blockCauseInfo;
}
