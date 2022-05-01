package com.vvt.jpa.query;

import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vvt.jpa.exception.SearchQueryException;

public class QueryBuilder {
	private static final String OR_OP =" OR ";
	private static final String AND_OP =" AND ";
	private Map<String, Object> paramMap = new HashMap<String, Object>();
	private StringBuilder whereBuilder;
	private String alias="";
	private String[] select;
	private boolean isCount = false;
	private Class<?> entityClass;
	private List<SortBy> sorts = new ArrayList<SortBy>();
	
	public QueryBuilder(Class<?> entityClass, String alias) {
		if(alias.contains(" "))
			throw new SearchQueryException("alias has space words");
		this.alias = alias;
		this.entityClass = entityClass;
	}
	/**
	 * Tạo query
	 * @return
	 */
	public String build() {
		StringBuilder sb = new StringBuilder("SELECT ");
		if(isCount) {
			sb.append("COUNT( ");
			sb.append(this.alias);
			sb.append(')');
		}else if(select == null|| select.length==0) {
			sb.append(alias);
		}else {
			boolean isFirst = true;
			for(String s: select) {
				if(isFirst) {
					isFirst = false;
				}else {
					sb.append(", ");
				}
				sb.append(alias);
				sb.append('.');
				sb.append(s);
			}
		}
		sb.append(" FROM ");
		sb.append(entityClass.getSimpleName());
		sb.append(' ');
		sb.append(alias);
		if(whereBuilder!=null)
			sb.append(whereBuilder.toString());
		//sort
		if(sorts!=null && sorts.size()>0) {
			sb.append(" ORDER BY");
			for(SortBy sort: sorts) {
				sb.append(' ');
				sb.append(this.alias);
				sb.append('.');
				sb.append(sort.getSortColumn());
				sb.append(' ');
				sb.append(sort.getSortType());
			}
		}
		return sb.toString();
	}
	/**
	 * Lấy giá trị field trong entity
	 * @param select
	 * @return
	 */
	public QueryBuilder select(String...select) {
		this.isCount = false;
		this.select = select;
		return this;
	}
	public QueryBuilder selectCount() {
		isCount = true;
		return this;
	}
	/**
	 * Tạo câu điều kiện WHERE
	 * @param searchObject
	 * @return
	 */
	public QueryBuilder where(Object searchObject) {
		this.whereBuilder = new StringBuilder(" WHERE ");
		//Lấy dữ liệu của field trong searchObject
		List<SearchColumnInfo> columnInfos = getSearchColumnInfos(searchObject);
		//group mặc định
		String groupName = "";
		String[] splitCurrentGroup = groupName.split("[.]");
		// Liên kết các điều kiện mặc định
		String groupType = AND_OP;
		boolean isFirsts[] = new boolean[10];
		// đánh dấu số ký tự '(' trừ đi số ký tự ')'
		int bracketLv =0;
		isFirsts[0] = true;
		//Thêm field vào where
		for(SearchColumnInfo col: columnInfos) {
			
			if(!col.getGroupName().equals(groupName)) {//Nếu khác tên với group trước đó
				//tìm level của group chung nhỏ nhất =======================================
				String[] splitNewGroup = col.getGroupName().split("[.]");
				int newBracketLv = 0;
				while(splitCurrentGroup.length>newBracketLv+1 
						&& splitNewGroup.length>newBracketLv+1
						&& splitCurrentGroup[newBracketLv+1].equals( splitNewGroup[newBracketLv+1])) {
					newBracketLv++;
				}
				
				//thêm n1 lần )  để trở về group chung ======================================
				while(bracketLv!= newBracketLv) {
					whereBuilder.append(')');
					isFirsts[bracketLv] = true;
					bracketLv--;
				}
	
				//thêm AND/OR để liên kết group start========================================
				groupType = splitNewGroup[bracketLv].startsWith("OR")?OR_OP:AND_OP;
				
				if(isFirsts[bracketLv])
					isFirsts[bracketLv] = false;
				else
					whereBuilder.append(groupType);

				//thêm n2 lân ( để đến group mới=============================================
				for(int i =bracketLv +1 ; i< splitNewGroup.length;i++) {
					whereBuilder.append('(');
					bracketLv++;
				}
				whereBuilder.append(' ');
				
				// gán group mặc định và phép liên kết mặc định===============================
				groupName = col.getGroupName();
				groupType = splitNewGroup[bracketLv].startsWith("OR")?OR_OP:AND_OP;
				splitCurrentGroup = splitNewGroup;
			}else {//Nếu trùng tên với group trước đó
				// Thêm AND/OR để liên kết điều kiện
				if(isFirsts[bracketLv])
					isFirsts[bracketLv] = false;
				else
					whereBuilder.append(groupType);
			}
			// Thêm điều kiện dạng alias.field = :paramName
			String paramName = alias + '_' + col.getParamName();
			whereBuilder.append(this.alias);
			whereBuilder.append('.');
			whereBuilder.append(col.getName());
			whereBuilder.append(' ');
			whereBuilder.append(col.getConditionType().getValue());
			switch(col.getConditionType()) {
			case HasContain:
				whereBuilder.append(" CONCAT( '%',");
				whereBuilder.append(" :");
				whereBuilder.append(paramName);
				whereBuilder.append(", '%')");
				break;
			case StartWith:
				whereBuilder.append(" CONCAT( '%',");
				whereBuilder.append(" :");
				whereBuilder.append(paramName);
				whereBuilder.append(")");
				break;
			case EndWith:
				whereBuilder.append(" CONCAT(");
				whereBuilder.append(" :");
				whereBuilder.append(paramName);
				whereBuilder.append(", '%')");
				break;
			default:
				whereBuilder.append(" :");
				whereBuilder.append(paramName);
			}
			
			this.paramMap.put(paramName, col.getValue());
		}// end for index = col
		
		// Nếu kết thúc điều kiện mà chưa đóng hêt ngoặc thì thêm )
		while(bracketLv!=0) {
			whereBuilder.append(')');
			bracketLv--;
		}
		return this;
	}
	
	public QueryBuilder orderBy(List<SortBy> sorts) {
		this.sorts = sorts;
		return this;
	}
	
	/**
	 * Lấy thông tin field
	 * @param o
	 * @return
	 */
	public static List<SearchColumnInfo> getSearchColumnInfos(Object o){
		List<SearchColumnInfo> list = new ArrayList<SearchColumnInfo>();
		Class<?> currentClass = o.getClass();
		while(currentClass!= Object.class) {
			for(Field field : o.getClass().getDeclaredFields()) {
				try {
					list.add(new SearchColumnInfo(field, o));
				}catch(Exception e) {
					//e.printStackTrace();
				}
			}
			currentClass = currentClass.getSuperclass();
		}
		//Sắp xếp theo group
		list.sort((c1,c2)->{
			int	sort1 = c1.getGroupName().compareTo(c2.getGroupName());
			int sort2 = c1.getSearchNo() - c2.getSearchNo();
			return sort1!=0?sort1:sort2; 
		});
		return list;
	}
	public StringBuilder getWhereBuilder() {
		return whereBuilder;
	}
	public void setWhereBuilder(StringBuilder whereBuilder) {
		this.whereBuilder = whereBuilder;
	}
	public Map<String, Object> getParamMap() {
		return paramMap;
	}
	public void setParamMap(Map<String, Object> paramMap) {
		this.paramMap = paramMap;
	}
}
