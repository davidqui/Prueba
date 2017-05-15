package com.laamware.ejercito.doc.web.entity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface LaamCreate {

	public int order();
	
}
