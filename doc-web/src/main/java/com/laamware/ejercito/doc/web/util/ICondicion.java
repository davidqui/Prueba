package com.laamware.ejercito.doc.web.util;

import java.util.Map;

/**
 * Representa una condición de transición
 * 
 * @author acpreda
 *
 */
public interface ICondicion {

	boolean cumple(Map<String, String> vars, Object facade) throws Exception;

}
