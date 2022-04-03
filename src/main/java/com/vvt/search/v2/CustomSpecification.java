package com.vvt.search.v2;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.FetchParent;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.vvt.search.v2.column.SearchColumn;
import com.vvt.search.v2.column.SearchColumnGroup;
import com.vvt.search.v2.column.SearchColumnInfo;
import com.vvt.search.v2.column.SearchJoinColumn;
import com.vvt.search.v2.column.SearchJoinColumnInfo;
import com.vvt.search.v2.fetch.FetchAttribute;
import com.vvt.search.v2.type.CompareType;
import com.vvt.search.v2.type.SearchArrayType;
/**
 * 
 * @author Vu van thuong
 *
 *
 * @param <T>
 */
public class CustomSpecification<T> implements Specification<T>{
	private static final long serialVersionUID = 1L;
	private Object filter;
	private final Set<FetchAttribute> fetchFields;
	public Set<FetchAttribute> getFetchFields() {
		return fetchFields;
	}
	public Object getFilter() {
		return filter;
	}
	public void setFilter(Object filter) {
		this.filter = filter;
	}
	/**
	 * return sub fetch
	 * @param attributeName
	 * @param joinType
	 * @return
	 */
	public FetchAttribute addSubFetch(String attributeName, JoinType joinType) {
		FetchAttribute attribute= new FetchAttribute(attributeName, joinType);
		fetchFields.add(attribute);
		return attribute;
	}
	/**
	 * 
	 * @param filter must not null 
	 */
	public CustomSpecification(Object filter) {
		this.setFilter(filter);
		this.fetchFields= new HashSet<>();
	}
	/**
	 * @exception com.vvt.search.v2.exception.SearchException
	 */
	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		for(FetchAttribute name: fetchFields) {
			addFetch(root, name);
		}
		query.distinct(true);
		return CustomSpecification.toCustomPredicate(root, cb, filter);
	}
	/**
	 * 
	 * @param root
	 * @param fetchAttr
	 * @return
	 */
	private void addFetch(FetchParent<?,?> root, FetchAttribute fetchAttr) {
		FetchParent<?,?> fetch = root.fetch(fetchAttr.getAttribute(), fetchAttr.getJoinType());
		for(FetchAttribute sub: fetchAttr.getSubFetchAttribute()) {
			addFetch(fetch, sub);
		}
	}
	
	/**
	 * get predicate from filter
	 * @param root
	 * @param cb
	 * @param filter
	 * @return
	 * @exception com.vvt.search.v2.exception.SearchException
	 */
	public static Predicate toCustomPredicate(From<?,?> root, CriteriaBuilder cb, Object filter) {
		Predicate predicate = cb.conjunction();// and predicate
		Map<String, SearchColumnGroup> searchGroups= new HashMap<>();
		// for each in all fields
		
		for(Field field: filter.getClass().getDeclaredFields()) {
			//*****************************************************
			// get name and value field
			field.setAccessible(true);
			try {
				SearchColumn searchColumn = field.getAnnotation(SearchColumn.class);
				if(searchColumn!=null) {
					if(!searchGroups.containsKey(searchColumn.group()))
						searchGroups.put(searchColumn.group(), new SearchColumnGroup());
					searchGroups.get(searchColumn.group())
						.getColumnInfos().add(new SearchColumnInfo(field.getName(), field.get(filter), searchColumn));
					
				}
					
				SearchJoinColumn searchJoinColumn = field.getAnnotation(SearchJoinColumn.class);
				if(searchJoinColumn!=null) {
					if(!searchGroups.containsKey(searchJoinColumn.group()))
						searchGroups.put(searchJoinColumn.group(), new SearchColumnGroup());
					searchGroups.get(searchJoinColumn.group())
						.getJoinColumnInfos().add(new SearchJoinColumnInfo(field.getName(), field.get(filter), searchJoinColumn));
				}
				
			} catch (IllegalArgumentException | IllegalAccessException e) {
				continue;
			}
		}
		// add group to predicate
		searchGroups.forEach((key, group)->{
			Predicate subPredicate;
			if(key.length()==0) // no group
				subPredicate = predicate;
			else {// "OR" group
				subPredicate = cb.disjunction();
				predicate.getExpressions().add(subPredicate);
			}
			group.getColumnInfos()
			.stream()
			.filter(info-> info.check() )
			.sorted((c1, c2)-> c2.getPriority()-c1.getPriority())
			.forEach(info->{		
				subPredicate.getExpressions().add(
						conditionWithColumn(root, cb, info));
			});
			group.getJoinColumnInfos()
			.stream()
			.filter(info-> info.check() )
			.sorted((c1, c2)-> c2.getPriority()-c1.getPriority())
			.forEach(info->{		
				subPredicate.getExpressions().add(
						conditionWithJoinColumn(root, cb, info));
			});
		});
		// return 
		return predicate;

	}
	
	/**
	 * 
	 * @param root
	 * @param cb
	 * @param info
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Expression<Boolean> conditionWithColumn(From<?, ?> root, CriteriaBuilder cb, SearchColumnInfo info) {
		// get function return Expression<Boolean> >
		final Function<Object, Expression<Boolean>> function;
		switch(info.getCompareType()) {
		case EqualGreaterThan:
			function = (item) -> cb.greaterThanOrEqualTo(root.get(info.getName()), (Comparable)item);
			break;
		case EqualLessThan:
			function = (item) -> cb.lessThanOrEqualTo(root.get(info.getName()), (Comparable)item);
			break;
		case GreaterThan:
			function = (item) -> cb.greaterThan(root.get(info.getName()), (Comparable)item);
			break;
		case LessThan:
			function = (item) -> cb.lessThan(root.get(info.getName()), (Comparable)item);
			break;
		case NotEqual:
			function = (item) -> cb.notEqual(root.get(info.getName()), item);
			break;
		case Like:
			function = (item) -> cb.like(root.get(info.getName()), item.toString());
			break;
		case HasContains:
			function = (item) -> cb.like(root.get(info.getName()), '%'+item.toString()+'%');
			break;
		case Equal:// equal
			function = (item) -> cb.equal(root.get(info.getName()), item);
			break;
		default: //is null
			function = (item) -> info.getValue()==null? cb.isNull(root.get(info.getName())):cb.isNotNull(root.get(info.getName()));
		}
		List<Object> items = new ArrayList<>();
		if(info.getValue().getClass().isArray()) {
			for(int i =0; i<Array.getLength(info.getValue());i++)
				items.add(Array.get(info.getValue(),i));
		}else if(info.getValue() instanceof Collection<?>) {
			((Collection<?>)info.getValue())
				.forEach((item)-> items.add(item));
		}else
			return function.apply(info.getValue());
		// if in
		if( info.getArrayType()==SearchArrayType.In 
			&& info.getCompareType()==CompareType.Equal ) {
			return root.get(info.getName()).in(items);
		}
		// if not in
		if(	info.getArrayType()== SearchArrayType.NotIn
			&& info.getCompareType()==CompareType.Equal ) {
			return root.get(info.getName()).in(items).not();
		}
		// join items
		Expression<Boolean> condition;
		switch(info.getArrayType()) {
		case All:
			condition= items.stream().map(function).reduce(cb.and(), (e1,e2)->cb.and(e1, e2));
			break;
		case NotAll:
			condition= cb.not(items.stream().map(function).reduce(cb.and(), (e1,e2)->cb.and(e1, e2)));
			break;
		case NotIn:
		case NotAny:
			condition= cb.not(items.stream().map(function).reduce(cb.or(), (e1,e2)->cb.or(e1, e2)));
			break;
		default:
			condition= items.stream().map(function).reduce(cb.or(), (e1,e2)->cb.or(e1, e2));
		}
		if(info.isOrIsNull())
			condition = cb.or(cb.isNull(root.get(info.getName())),condition);
		return condition;
	}
	/**
	 * 
	 * @param root
	 * @param cb
	 * @param info
	 * @return
	 */
	public static Expression<Boolean> conditionWithJoinColumn(From<?, ?> root, CriteriaBuilder cb, SearchJoinColumnInfo info) {
		// join
		Join<?,?> join = root.join(info.getName(), info.getJoinType());
		// get function return  Expression<Boolean>> 
		final Function<Object, Expression<Boolean>> function;
		switch(info.getCompareType()) {
		case Equal:
			function = (item) -> toCustomPredicate(join, cb, item);
			break;
		case NotEqual:
			function = (item) -> toCustomPredicate(join, cb, item).not();
			break;
		default: //is null
			function = (item) -> info.getValue()==null? cb.isNull(root.get(info.getName())):cb.isNotNull(root.get(info.getName()));
		}
		List<Object> items = new ArrayList<>();
		if(info.getValue().getClass().isArray()) {
			for(int i =0; i<Array.getLength(info.getValue());i++)
				items.add(Array.get(info.getValue(),i));
		}else if(info.getValue() instanceof Collection<?>) {
			((Collection<?>)info.getValue())
				.forEach((item)-> items.add(item));
		}else
			return function.apply(info.getValue());
		// join items
		Expression<Boolean> condition;
		switch(info.getArrayType()) {
		case All:
			condition= items.stream().map(function).reduce(cb.and(), (e1,e2)->cb.and(e1, e2));
			break;
		case NotAll:
			condition= cb.not(items.stream().map(function).reduce(cb.and(), (e1,e2)->cb.and(e1, e2)));
			break;
		case NotIn:
		case NotAny:
			condition= cb.not(items.stream().map(function).reduce(cb.or(), (e1,e2)->cb.or(e1, e2)));
			break;
		default:
			condition= items.stream().map(function).reduce(cb.or(), (e1,e2)->cb.or(e1, e2));
		}
		if(info.isOrIsNull())
			condition = cb.or(cb.isNull(root.get(info.getName())),condition);
		return condition;
	}

}
