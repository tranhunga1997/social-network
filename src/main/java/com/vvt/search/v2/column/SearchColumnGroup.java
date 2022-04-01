package com.vvt.search.v2.column;

import java.util.ArrayList;
import java.util.List;

public class SearchColumnGroup {
	private List<SearchColumnInfo> columnInfos;
	private List<SearchJoinColumnInfo> joinColumnInfos;
	public List<SearchColumnInfo> getColumnInfos() {
		return columnInfos;
	}
	public void setColumnInfos(List<SearchColumnInfo> columnInfos) {
		this.columnInfos = columnInfos;
	}
	public List<SearchJoinColumnInfo> getJoinColumnInfos() {
		return joinColumnInfos;
	}
	public void setJoinColumnInfos(List<SearchJoinColumnInfo> joinColumnInfos) {
		this.joinColumnInfos = joinColumnInfos;
	}
	public SearchColumnGroup() {
		columnInfos= new ArrayList<>();
		joinColumnInfos = new ArrayList<>();
	}
}
