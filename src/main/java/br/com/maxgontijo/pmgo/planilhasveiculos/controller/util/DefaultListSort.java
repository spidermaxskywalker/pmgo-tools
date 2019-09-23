package br.com.maxgontijo.pmgo.planilhasveiculos.controller.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Comparator;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface DefaultListSort {
	Class<? extends Comparator<?>> comparator() default DefaultListSortByAttributeComparator.class;

	String[] args() default {};

	String[] value() default {};
}
