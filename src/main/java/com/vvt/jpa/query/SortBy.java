package com.vvt.jpa.query;

import java.util.ArrayList;
import java.util.List;

public class SortBy {
	private String sortType;
	private String sortColumn;
	public String getSortType() {
		return sortType;
	}
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
	public String getSortColumn() {
		return sortColumn;
	}
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}
	
	public static List<SortBy> sortASC(String ...fields){
		List<SortBy> list = new ArrayList<SortBy>();
		for(String s: fields) {
			SortBy sortBy = new SortBy();
			sortBy.setSortType("ASC");
			sortBy.setSortColumn(s);
			list.add(sortBy);
		}
		return list;
	}
	public static List<SortBy> sortDESC(String ...fields){
		List<SortBy> list = new ArrayList<SortBy>();
		for(String s: fields) {
			SortBy sortBy = new SortBy();
			sortBy.setSortType("DESC");
			sortBy.setSortColumn(s);
			list.add(sortBy);
		}
		return list;
	}
}
