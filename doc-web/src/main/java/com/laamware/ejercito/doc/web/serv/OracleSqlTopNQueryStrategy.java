package com.laamware.ejercito.doc.web.serv;

import org.springframework.stereotype.Component;

/**
 * Implementación para Oracle de {@link SqlTopNQueryStrategy}
 * 
 * @author acpreda
 * 
 */
@Component
public class OracleSqlTopNQueryStrategy implements SqlTopNQueryStrategy {

	@Override
	public String wrap(String sqlWithoutOrderBy, String orderBy,
			boolean isOrderByUnique, int page, int resultsPerPage) {

		Integer maxRow = (page + 1) * resultsPerPage;
		Integer minRow = page * resultsPerPage;

		StringBuilder sb = new StringBuilder();
		sb.append("select * from (select topN___a.*, rownum rnum from (");
		sb.append(sqlWithoutOrderBy);
		sb.append(" order by ");
		sb.append(orderBy);
		if (isOrderByUnique == false) {
			sb.append(", rowid");
		}
		sb.append(") topN___a where rownum <= ").append(maxRow)
				.append(" ) where rnum >= ").append(minRow);
		return sb.toString();
	}

}
