package com.socialnetwork.common.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagination<T> {
	private int totalPages;
	private long totalElements;
	private List<T> datas;
}
