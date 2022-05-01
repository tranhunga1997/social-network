package com.vvt.jpa.query;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 
 * @author vu van thuong
 *
 */
@Target({ FIELD })
@Retention(RUNTIME)
@Documented
public @interface SearchColumn {
	String name() default "";
	ConditionType condition() default ConditionType.EQ;
	String paramName() default "";
	String groupName() default "";
	int searchNo() default 0;
}
