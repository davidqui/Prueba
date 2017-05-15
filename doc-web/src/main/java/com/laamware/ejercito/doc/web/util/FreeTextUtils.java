package com.laamware.ejercito.doc.web.util;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.loader.criteria.CriteriaJoinWalker;
import org.hibernate.loader.criteria.CriteriaQueryTranslator;
import org.hibernate.persister.entity.OuterJoinLoadable;

/**
 * Clase que se encaraga de validar los caracteres ingresados en las búsquedas
 * por término
 * 
 * 
 */
public class FreeTextUtils {

	public static Criterion buildFreeTextCriterion(String column, String term) {
		String sqlCondition = buildFreeTextANDSQLCondition(column, term);
		Criterion restriction = Restrictions.sqlRestriction(sqlCondition);
		return restriction;
	}

	public static String[] extractWords(String str) {
		String[] words = StringUtils.split(str, ' ');
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			if (StringUtils.isAlphanumeric(word) == false) {
				word = word.replaceAll("[^\\p{L}\\p{Nd}]+", "");
				words[i] = word;
			}
		}
		return words;
	}

	public static String buildFreeTextANDSQLCondition(String column, String term) {
		return buildFreeTextANDSQLCondition(column, term, 1);
	}

	public static String buildFreeTextANDSQLCondition(String column,
			String term, int idx) {
		String[] words = extractWords(term);
		StringBuilder sb = new StringBuilder("CONTAINS(").append(column)
				.append(",'");
		boolean first = true;
		for (int i = 0; i < words.length; i++) {
			if (!first) {
				sb.append(" AND ");
			} else {
				first = false;
			}
			sb.append(words[i]).append("%");
		}
		sb.append("', " + idx + ") > 0");
		return sb.toString();
	}

	public static String buildFreeTextORSQLCondition(String column, String term) {
		return buildFreeTextORSQLCondition(column, term, 1);
	}

	public static String buildFreeTextORSQLCondition(String column,String term, int idx) {
		
		String[] words = extractWords(term);
		StringBuilder sb = new StringBuilder("CONTAINS(").append(column).append(",'");
		boolean first = true;
		for (int i = 0; i < words.length; i++) {
			if (!first) {
				sb.append(" OR ");
			} else {
				first = false;
			}
			sb.append(words[i]).append("%");
		}
		sb.append("', " + idx + ") > 0");
		return sb.toString();
	}



	public static String getSQLFromCriteria(Criteria criteria) {
		CriteriaImpl criteriaImpl = (CriteriaImpl) criteria;
		SessionImplementor session = criteriaImpl.getSession();
		SessionFactoryImplementor factory = session.getFactory();
		CriteriaQueryTranslator translator = new CriteriaQueryTranslator(
				factory, criteriaImpl, criteriaImpl.getEntityOrClassName(),
				CriteriaQueryTranslator.ROOT_SQL_ALIAS);
		String[] implementors = factory.getImplementors(criteriaImpl
				.getEntityOrClassName());

		CriteriaJoinWalker walker = new CriteriaJoinWalker(
				(OuterJoinLoadable) factory.getEntityPersister(implementors[0]),
				translator, factory, criteriaImpl, criteriaImpl
						.getEntityOrClassName(), session
						.getLoadQueryInfluencers());

		String sql = walker.getSQLString();
		return sql;
	}

}
