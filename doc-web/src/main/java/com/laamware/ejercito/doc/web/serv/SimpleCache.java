package com.laamware.ejercito.doc.web.serv;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SimpleCache<T> {
	
	static class Entry<T> {
		Date date;
		T value;
	}
	
	Map<String, Entry<T>> cache = new HashMap<String, Entry<T>>();
	
	public T get(String key, Date date){
		Entry<T> e = cache.get(key);
		if(e == null) {
			return null;
		}
		
		if(e.date.getTime() < date.getTime()) {
			cache.remove(key);
			return null;
		}
		
		return e.value;
	}

	public void put(String key, T value, Date date) {
		cache.remove(key);
		Entry<T> e = new Entry<T>();
		e.date = date;
		e.value = value;
		cache.put(key, e);
	}
	
}
