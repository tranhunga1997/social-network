package com.socialnetwork.general.user.search;

import com.vvt.jpa.query.ConditionType;
import com.vvt.jpa.query.SearchColumn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionSearch {
	@SearchColumn(condition = ConditionType.HasContain)
	private String name;
}
