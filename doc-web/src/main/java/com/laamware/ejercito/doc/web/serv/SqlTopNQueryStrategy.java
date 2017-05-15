package com.laamware.ejercito.doc.web.serv;

/**
 * Estrategia del Top-N query
 * 
 * @author acpreda
 * 
 */
public interface SqlTopNQueryStrategy {

	/**
	 * Envuelve una sentencia SQL para que se haga paginación Top-N
	 * 
	 * @param sql
	 * @param page
	 * @param resultsPerPage
	 * @return
	 */
	String wrap(String sqlWithoutOrderBy, String orderBy,
			boolean isOrderByUnique, int page, int resultsPerPage);

}
