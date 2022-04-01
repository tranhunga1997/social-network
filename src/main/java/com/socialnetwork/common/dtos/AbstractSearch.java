package com.socialnetwork.common.dtos;

import lombok.Data;

@Data
public abstract class AbstractSearch {
	private int dataPageNo;
	private int dataPageSize;
	private String sortBy;
	private String sortType;
}
