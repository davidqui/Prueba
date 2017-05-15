package com.laamware.ejercito.doc.web.util;

import java.util.HashMap;
import java.util.Map;

public class StringMapBuilder {

	private HashMap<String, Object> map;

	public StringMapBuilder() {
		this.map = new HashMap<String, Object>();
	}

	public StringMapBuilder add(String key, Object value) {
		this.map.put(key, value);
		return this;
	}

	public Map<String, Object> map() {
		return map;
	}

}
