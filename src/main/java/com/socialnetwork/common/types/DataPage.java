package com.socialnetwork.common.types;

import java.util.List;

import lombok.Data;

/**
 * 
 * @author thuong
 * @param <D>: Dto
 * @param <F>: SearchInput
 */
@Data
public class DataPage<D, S> {
	private S search;
	private List<D> data;
	private int dataPageSize;
	private int dataPageNo;
	private int totalPage;
	private int count;
	private long countAllPage;
}
