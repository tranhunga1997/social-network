package com.vvt.jpa;

import java.util.List;

import com.vvt.jpa.query.SortBy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractSearch {
	private int pageSize;
	private int pageNo;
	private List<SortBy> sorts;
}
