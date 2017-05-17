/**
 * 
 */
package com.laamware.ejercito.doc.web.util;

import java.util.Comparator;

/**
 * Comparador de número de versiones.
 * 
 * @author jgarcia@controltechcg.com
 * @since May 15, 2017
 *
 */
// 2017-05-15 jgarcia@controltechcg.com Issue #80 (SICDI-Controltech) feature-80
public class NumeroVersionComparator implements Comparator<String> {

	@Override
	public int compare(String version1, String version2) {
		String[] tokens1 = version1.split("\\.");
		String[] tokens2 = version2.split("\\.");

		if (tokens1.length < tokens2.length) {
			return -1;
		}

		if (tokens1.length > tokens2.length) {
			return 1;
		}

		for (int i = 0; i < tokens1.length; i++) {
			if (Integer.parseInt(tokens1[i]) < Integer.parseInt(tokens2[i])) {
				return -1;
			}

			if (Integer.parseInt(tokens1[i]) > Integer.parseInt(tokens2[i])) {
				return 1;
			}
		}

		return 0;
	}

}
