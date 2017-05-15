package com.laamware.ejercito.doc.web.entity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface LaamWidget {
	public String value();

	public String list() default "";

	public Class<?> type() default Object.class;
}
