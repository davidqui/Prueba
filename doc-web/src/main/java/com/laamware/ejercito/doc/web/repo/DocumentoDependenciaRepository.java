package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import com.laamware.ejercito.doc.web.entity.DocumentoDependencia;

public interface DocumentoDependenciaRepository extends GenJpaRepository<DocumentoDependencia, Integer> {

	/**
	 * Obtiene los documentos archivados por dependencia y TRD, ordenados de
	 * forma descendiente por la fecha de creación.
	 * 
	 * @param depId
	 *            ID de dependencia.
	 * @param trd
	 *            ID de la TRD.
	 * @return Lista de documentos archivados.
	 */
	/*
	 * 2017-05-05 jgarcia@controltechcg.com Issue #63 (SICDI-Controltech):
	 * Ordenamiento descendiente por fecha de creación del registro de archivo.
	 */
	List<DocumentoDependencia> findByDependenciaIdAndTrdIdOrderByCuandoDesc(Integer depId, Integer trd);

	/**
	 * Obtiene los documentos archivador por usuario y TRD, ordenados de forma
	 * descendiente por la fecha de creacíón.
	 * 
	 * @param quien
	 *            ID del usuario.
	 * @param trd
	 *            ID de la TRD.
	 * @return Lista de documentos archivados.
	 */
	/*
	 * 2017-05-11 jgarcia@controltechcg.com Issue #79 (SICDI-Controltech):
	 * Limitar la presentación de documentos archivados por usuario en sesión y
	 * TRD.
	 */
	List<DocumentoDependencia> findByQuienAndTrdIdOrderByCuandoDesc(Integer quien, Integer trd);

}
