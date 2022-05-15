package com.vvt.jpa;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageData<E>{
	
	private AbstractSearch searchObj;
	
	private long countAllRecord;
	private int countAllPage;
	private int pageSize;
	private int pageNo;
	private long offsetStart;
	private long offsetEnd;
	private List<E> data;
	
	public PageData() {
		super();
	}
	public PageData(AbstractSearch searchObj) {
		super();
		this.searchObj = searchObj;
	}
}
