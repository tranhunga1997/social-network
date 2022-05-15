package com.vvt.jpa.query;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.hibernate.annotations.ManyToAny;
import org.springframework.data.domain.Pageable;

import com.vvt.jpa.exception.SearchQueryException;
/**
 * 
 * @author akumu
 *
 * @param <E>; Entity class
 */
public class SearchQuery<E> {
	private Map<String, Object> paramMap = new HashMap<String, Object>();
	private CriteriaBuilder cb;
	private EntityManager em;
	private Root<E> root;
	private Map<String, From<?,?>> joinMap = new HashMap<String, From<?,?>>();
	private Class<E> entityClass;
	private Object searchObject;
	private Pageable pageable;

	public SearchQuery(Class<E> entityClass, EntityManager em) {
		this.em = em;
		this.entityClass = entityClass;
		cb = em.getCriteriaBuilder();

		// pageable
	}

	public SearchQuery<E> pageable(Pageable pageable) {
		this.pageable = pageable;
		return this;
	}

	/**
	 * Lấy số lượng bản ghi
	 * 
	 * @return
	 */
	public long getCount() {
		CriteriaQuery<Long> query = cb.createQuery(Long.class);
		Root<E> root = query.from(entityClass);
		joinMap.clear();
		// count
		query.multiselect(cb.count(root));
		Predicate wherePredicate = null;
		if (searchObject != null)
			wherePredicate = getWhere(root, searchObject);
		if (wherePredicate != null && wherePredicate.getExpressions().size() > 0)
			query.where(wherePredicate);

		TypedQuery<Long> queryResult = em.createQuery(query);
		// add parameter
		paramMap.forEach((pname, pvalue) -> {
			queryResult.setParameter(pname, pvalue);
		});
		return queryResult.getSingleResult();
	}

	/**
	 * Lấy dữ liệu
	 * 
	 * @return
	 */
	public List<E> getResultList() {
		CriteriaQuery<E> query = cb.createQuery(this.entityClass);
		Root<E> root = query.from(entityClass);
		joinMap.clear();
		Predicate wherePredicate = null;
		if (searchObject != null)
			wherePredicate = getWhere(root, searchObject);
		if (wherePredicate != null && wherePredicate.getExpressions().size() > 0)
			query.where(wherePredicate);
		// sort
		List<Order> orders = null;
		if (pageable != null)
			orders = getOrders(root, pageable);
		if (orders != null && orders.size() > 0)
			query.orderBy(orders);
		TypedQuery<E> queryResult = em.createQuery(query);
		// limit+ offset
		if (pageable != null) {
			queryResult.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
			queryResult.setMaxResults(pageable.getPageSize());
		}
		// add parameter
		paramMap.forEach((pname, pvalue) -> {
			queryResult.setParameter(pname, pvalue);
		});
		return queryResult.getResultList();
	}

	/**
	 * Lấy dữ liệu tuỳ chọn
	 * 
	 * @param fieldNames
	 * @return
	 */
	public List<Object[]> getResultList(String... fieldNames) {
		CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
		Root<E> root = query.from(entityClass);
		joinMap.clear();
		List<Selection<?>> selections = Arrays.asList(fieldNames).stream().map(fieldName -> {
			return root.get(fieldName);
		}).collect(Collectors.toList());
		query.multiselect(selections);
		Predicate wherePredicate = null;
		if (searchObject != null)
			wherePredicate = getWhere(root, searchObject);
		if (wherePredicate != null && wherePredicate.getExpressions().size() > 0)
			query.where(wherePredicate);
		// sort
		List<Order> orders = null;
		if (pageable != null)
			orders = getOrders(root, pageable);
		if (orders != null && orders.size() > 0)
			query.orderBy(orders);
		TypedQuery<Object[]> queryResult = em.createQuery(query);
		// limit+ offset
		if (pageable != null) {
			queryResult.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
			queryResult.setMaxResults(pageable.getPageSize());
		}
		// add parameter
		paramMap.forEach((pname, pvalue) -> {
			queryResult.setParameter(pname, pvalue);
		});
		return queryResult.getResultList();
	}

	/**
	 * Lấy dữ liệu trả về 1 bản ghi
	 * 
	 * @return
	 */
	public E getSingleResult() {
		CriteriaQuery<E> query = cb.createQuery(this.entityClass);
		Root<E> root = query.from(entityClass);
		joinMap.clear();
		Predicate wherePredicate = null;
		if (searchObject != null)
			wherePredicate = getWhere(root, searchObject);
		if (wherePredicate != null && wherePredicate.getExpressions().size() > 0)
			query.where(wherePredicate);
		// sort
		List<Order> orders = null;
		if (pageable != null)
			orders = getOrders(root, pageable);
		if (orders != null && orders.size() > 0)
			query.orderBy(orders);
		TypedQuery<E> queryResult = em.createQuery(query);
		// add parameter
		paramMap.forEach((pname, pvalue) -> {
			queryResult.setParameter(pname, pvalue);
		});
		return queryResult.getSingleResult();
	}

	/**
	 * Lấy dữ liệu trả về 1 bản ghi tuỳ chọn
	 * 
	 * @param fieldNames : cột trả về
	 * @return
	 */
	public Object[] getSingleResult(String... fieldNames) {
		CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
		Root<E> root = query.from(entityClass);
		joinMap.clear();
		List<Selection<?>> selections = Arrays.asList(fieldNames).stream().map(fieldName -> {
			return root.get(fieldName);
		}).collect(Collectors.toList());
		query.multiselect(selections);
		Predicate wherePredicate = null;
		if (searchObject != null)
			wherePredicate = getWhere(root, searchObject);
		if (wherePredicate != null && wherePredicate.getExpressions().size() > 0)
			query.where(wherePredicate);
		// sort
		List<Order> orders = null;
		if (pageable != null)
			orders = getOrders(root, pageable);
		if (orders != null && orders.size() > 0)
			query.orderBy(orders);
		TypedQuery<Object[]> queryResult = em.createQuery(query);
		// add parameter
		paramMap.forEach((pname, pvalue) -> {
			queryResult.setParameter(pname, pvalue);
		});
		return queryResult.getSingleResult();
	}

	/**
	 * Lấy dữ liệu tìm kiếm đầu vào
	 * 
	 * @param searchObject
	 * @return
	 */
	public SearchQuery<E> where(Object searchObject) {
		this.searchObject = searchObject;
		return this;
	}

	/**
	 * Lấy thông tin field
	 * 
	 * @param o
	 * @return
	 */
	public static List<SearchColumnInfo> getSearchColumnInfos(Object o) {
		List<SearchColumnInfo> list = new ArrayList<SearchColumnInfo>();
		Class<?> currentClass = o.getClass();
		while (currentClass != Object.class) {
			for (Field field : o.getClass().getDeclaredFields()) {
				try {
					list.add(new SearchColumnInfo(field, o));
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
			currentClass = currentClass.getSuperclass();
		}
		// Sắp xếp theo group
		list.sort((c1, c2) -> {
			int sort1 = c1.getGroupName().compareTo(c2.getGroupName());
			int sort2 = c1.getSearchNo() - c2.getSearchNo();
			return sort1 != 0 ? sort1 : sort2;
		});
		return list;
	}

	/**
	 * Lấy order từ pageable
	 * @param root
	 * @param pageable
	 * @return
	 */
	private List<Order> getOrders(Root<E> root, Pageable pageable) {
		// sort
		List<Order> orders = new ArrayList<Order>();
		pageable.getSort().forEach(o -> {
			String[] attrs = o.getProperty().split("[.]");
			Path<?> sortCol = root;
			for (String attr : attrs) {
				sortCol = sortCol.get(attr);
			}
			Order order = null;
			if (o.isAscending())
				order = cb.asc(sortCol);
			else
				order = cb.desc(sortCol);
			orders.add(order);
		});
		return orders;
	}
	/**
	 * 
	 * @param root
	 * @param searchObject
	 * @return
	 */
	private Predicate getWhere(Root<E> root, Object searchObject) {
		this.paramMap.clear();
		List<SearchColumnInfo> searchColumnInfos = getSearchColumnInfos(searchObject);
		// where
		// Lưu trữ điều kiện
		Map<String, Predicate> predicateMap = new HashMap<String, Predicate>();
		// predicate của group gốc(sử dụng trực tiếp cho where)
		Predicate wherePredicate = cb.conjunction();
		predicateMap.put("", wherePredicate);

		for (SearchColumnInfo columnInfo : searchColumnInfos) {
			// chia nhỏ group ngăn cách bởi dấu "." để lấy group cha(parent)
			String[] groupNameSplit = columnInfo.getGroupName().split("[.]");
			StringBuilder groupNameBuilder = new StringBuilder();
			Predicate parentPredicate = wherePredicate;
			for (int i = 0; i < groupNameSplit.length; i++) {
				// lấy tên group tiếp theo
				groupNameBuilder.append('.');
				groupNameBuilder.append(groupNameSplit[i]);
				// gán tên group hiện tại (current)
				String currentGroupName = groupNameBuilder.toString();
				// Lấy predicate của group hiện tại trong map
				Predicate currentPredicate = predicateMap.get(currentGroupName);
				// Nếu không tồn tại trong map thì tạo mới
				if (currentPredicate == null) {
					// tạo mới, lưu trong map
					currentPredicate = currentGroupName.matches("^.*OR_[^.]*$") ? cb.disjunction() : cb.conjunction();
					predicateMap.put(currentGroupName, currentPredicate);
					// lưu vào trong group cha trước đó
					parentPredicate.getExpressions().add(currentPredicate);
				}
				// gán predicate cha là predicate hiện tại, để thực hiện với group con tiếp theo
				parentPredicate = currentPredicate;
			} // end for index = i
				// sau khi thực hiện vòng for, parentPredicate là columnInfo.groupName
			parentPredicate.getExpressions().add(toExpresstionBoolean(columnInfo, root, cb));
		} // end for value = columnInfo

		return wherePredicate;
	}
	/**
	 * Chuyển thông tin tìm kiếm thành điều kiện
	 * 
	 * @param col
	 * @param root
	 * @param cb
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Expression<Boolean> toExpresstionBoolean(SearchColumnInfo col, Root<?> root, CriteriaBuilder cb) {
		paramMap.put(col.getParamName(), col.getValue());
		
		switch (col.getConditionType()) {
		case EndWith:
			ParameterExpression<String> param1 = cb.parameter(String.class, col.getParamName());
			return cb.like( getPath(root, col.getName()), cb.concat("%", param1));
		case EQ:
			ParameterExpression<Comparable> param2 = cb.parameter(Comparable.class, col.getParamName());
			return cb.equal( getPath(root, col.getName()), param2);
		case GE:
			ParameterExpression<Comparable> param3 = cb.parameter(Comparable.class, col.getParamName());
			return cb.greaterThanOrEqualTo( getPath(root, col.getName()), param3);
		case GT:
			ParameterExpression<Comparable> param4 = cb.parameter(Comparable.class, col.getParamName());
			return cb.greaterThan( getPath(root, col.getName()), param4);
		case LE:
			ParameterExpression<Comparable> param5 = cb.parameter(Comparable.class, col.getParamName());
			return cb.lessThanOrEqualTo( getPath(root, col.getName()), param5);
		case LT:
			ParameterExpression<Comparable> param6 = cb.parameter(Comparable.class, col.getParamName());
			return cb.lessThan( getPath(root, col.getName()), param6);
		case HasContain:
			ParameterExpression<String> param7 = cb.parameter(String.class, col.getParamName());
			return cb.like( getPath(root, col.getName()), cb.concat(cb.concat("%", param7), "%"));
		case Like:
			ParameterExpression<String> param8 = cb.parameter(String.class, col.getParamName());
			return cb.like( getPath(root, col.getName()), param8);
		case StartWith:
			ParameterExpression<String> param9 = cb.parameter(String.class, col.getParamName());
			return cb.like( getPath(root, col.getName()), cb.concat(param9, "%"));
		case In:
			ParameterExpression<Collection> param10 = cb.parameter(Collection.class, col.getParamName());
			return getPath(root, col.getName()).in(param10);
		case NotIn:
			ParameterExpression<Collection> param11 = cb.parameter(Collection.class, col.getParamName());
			return getPath(root, col.getName()).in(param11).not();
		case NEQ:
			ParameterExpression<Comparable> param12 = cb.parameter(Comparable.class, col.getParamName());
			return cb.equal( getPath(root, col.getName()), param12).not();
		default:
			return null;
		}
	}
	/**
	 * get path of column
	 * @param <Y>
	 * @param root
	 * @param name
	 * @return
	 */
	private <Y> Path<Y> getPath(From<?,?> root, String name) {
		try {
			Class<?> clazz = entityClass;
			From<?,?> join = root;
			Path<?> path;
			// index của ký tự đằng sau ký tự '.'
			int startIndex = 0;
			//get join
			for(int i = 0; i< name.length(); i++) {
				if(name.charAt(i) != '.') {
					continue;
				}
				
				String fieldName = name.substring(startIndex, i);
				String joinName = name.substring(0, i);
				Field field = clazz.getDeclaredField(fieldName);
				
				
				if(field.getAnnotation(ManyToMany.class) != null || field.getAnnotation(OneToMany.class) != null ) {
					// nếu field là dạng List<Entity>
					ParameterizedType type =  (ParameterizedType) field.getGenericType();
					clazz = (Class<?>) type.getActualTypeArguments()[0];
				}else if(field.getAnnotation(ManyToOne.class) != null || field.getAnnotation(OneToOne.class) != null){
					// Field là dạng Entity
					clazz = field.getType();
				}else {
					// Field không phải entity
					break;
				}
				
				From<?, ?> tempJoin = joinMap.get(joinName);
				if(tempJoin == null) {// chưa tồn tại trong joinMap
					tempJoin = join.join(fieldName);
					joinMap.put(joinName, tempJoin);
				}
				join = tempJoin;
				startIndex = i + 1;	
			}
			path = join;
			//getPath
			for(int i = startIndex; i< name.length(); i++) {
				if(name.charAt(i) != '.') {
					continue;
				}
				path = path.get(name.substring( startIndex, i));
				startIndex = i + 1;
			}
			
			return path.get(name.substring( startIndex, name.length()));
		}catch(Exception e) {
			throw new SearchQueryException("Can't get path of column["+name+"]", e);
		}
	}
}
