package com.socialnetwork.common.entities.blockcause;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
/**
 * 
 * @author thuong
 *
 */
@Entity
@Table(name="m_block_cause_info")
@Data
public class BlockCauseInfo {
	//block_cause_id
	@Id
	@Column(name="block_cause_id")
	private Integer blockCauseId;
	//block_title]
	@Column(name="block_title", length = 100, nullable = false)
	private String blockTitle;
	//block_content
	@Column(name="block_content", length = 500, nullable = false)
	private String blockContent;

	
}
