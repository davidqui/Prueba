package com.laamware.ejercito.doc.web.serv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.repo.DependenciaRepository;

/**
 * Servicio para las operaciones de negocio de dependencias.
 * 
 * @author jgarcia@controltechcg.com
 * @since May 15, 2017
 */
// 2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech) feature-78
@Service
public class DependenciaService {

	@Autowired
	private DependenciaRepository dependenciaRepository;

	/**
	 * Busca la unidad de una dependencia.
	 * 
	 * @param dependencia
	 *            Dependencia.
	 * @return Dependencia unidad.
	 */
	public Dependencia buscarUnidad(Dependencia dependencia) {
		Dependencia unidad = dependencia;
		Integer padreID = dependencia.getPadre();

		while (padreID != null) {
			unidad = dependenciaRepository.findOne(padreID);
			padreID = unidad.getPadre();
		}

		return unidad;
	}

}
