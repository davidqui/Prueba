package com.laamware.ejercito.doc.web.serv;

/**
 * Clase que se encarga de crear excepciones de aplicación dependiendo de los
 * códigos de excepción que arroja la base de datos
 * 
 * @author acpreda
 *
 */
public class DatabaseExceptionMapper {

	public static DatabaseException map(Exception e) {
		// TODO: implementar el switch para determinar el tipo de excepción
		return new DatabaseException("Excepción de la base de datos", e);
	}
	
}
