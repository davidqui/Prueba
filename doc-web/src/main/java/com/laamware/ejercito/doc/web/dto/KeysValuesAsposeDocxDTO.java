package com.laamware.ejercito.doc.web.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author rafael blanco
 *
 */
public final class KeysValuesAsposeDocxDTO {
	
	private List<String> claves;
	private List<Object> values;
	
	public KeysValuesAsposeDocxDTO(  ) {
		claves = new ArrayList<>();
		values = new ArrayList<>();
	}

	/**
	 * 
	 * @param nombre
	 * @param value
	 */
	public void put( String nombre, Object value ){
		
		claves.add(nombre);
		values.add(value);
	}
	
	public String[] getNombres() {
		String[] s = claves.toArray(new String[claves.size()]);
		return s;
	}

	public Object[] getValues() {
		return values.toArray();
	}
	
}
