package com.laamware.ejercito.doc.web.serv;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.laamware.ejercito.doc.web.entity.Trd;
import com.laamware.ejercito.doc.web.util.NumeroVersionComparator;

/**
 * Servicio para las operaciones de las TRD.
 * 
 * @author jgarcia@controltechcg.com
 * @since May 15, 2017
 */
// 2017-05-15 jgarcia@controltechcg.com Issue #80 (SICDI-Controltech) feature-80
@Service
public class TRDService {

	/**
	 * Comparador de código de TRD.
	 * 
	 * @author jgarcia@controltechcg.com
	 * @since May 15, 2017
	 */
	private class TrdCodigoComparator implements Comparator<Trd> {

		private NumeroVersionComparator numeroVersionComparator = new NumeroVersionComparator();

		@Override
		public int compare(Trd trd1, Trd trd2) {
			final String codigo1 = trd1.getCodigo();
			final String codigo2 = trd2.getCodigo();
			return numeroVersionComparator.compare(codigo1, codigo2);
		}
	}

	/**
	 * Ordena la lista de TRD por el código, en ordenamiento tipo número de
	 * versión.
	 * 
	 * @param trds
	 *            Lista de TRD.
	 */
	public void ordenarPorCodigo(List<Trd> trds) {
		Collections.sort(trds, new TrdCodigoComparator());
	}
}
