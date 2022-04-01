package com.vvt.search.v2.column;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.persistence.criteria.JoinType;

import com.vvt.search.v2.type.CompareJoinType;
import com.vvt.search.v2.type.SearchArrayType;

/**
 * 
 * @author vu van thuong
 *
 */
@Target({ FIELD, METHOD })
@Retention(RUNTIME)
@Documented
public @interface SearchJoinColumn {
	String name() default "";
	/**
	 * Table column Equal(==) or Smaller(<) or  Greater(>).... this column
	 * default Equal
	 * @return
	 */
	CompareJoinType compare() default CompareJoinType.Equal;
	JoinType joinType() default JoinType.LEFT;
	SearchArrayType arrayType() default SearchArrayType.All;
	/** return:field is null or field <compare> ?
	 * @return
	 */
	boolean orIsNull() default false;
	
	String group() default "";
	
	int priority() default 0;
}
