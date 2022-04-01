package com.socialnetwork.common.entities.sequence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="m_sequence_info")
@Data
public class SequenceInfo {
	@Id
	@Column(name= "sequence_id",length = 10)
	private String sequenceId;
	@Column(name="current_num")
	private long currentNum;
	@Column(name="max_num")
	private long maxNum;
}
