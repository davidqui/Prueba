package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.DocumentoEnConsulta;

/**
 * Repositorio para {@link DocumentoEnConsulta}
 * 
 * @author jgarcia@controltechcg.com
 * @since Abril 19, 2017
 *
 */
// 2017-04-18 jgarcia@controltechcg.com Issue #50 (SICDI-Controltech)
public interface DocumentoEnConsultaRepository extends GenJpaRepository<DocumentoEnConsulta, Integer> {

	/**
	 * Busca el registro en consulta activo para un documento.
	 * 
	 * @param documento
	 *            Documento.
	 * @return Registro en consulta activo del documento, o {@code null} si no
	 *         hay un registro correspondiente.
	 */
	DocumentoEnConsulta findByDocumentoAndActivoTrue(Documento documento);

	/**
	 * Busca el registro en consulta para un documento.
	 * 
	 * @param documento
	 *            Documento.
	 * @return Registro en consulta del documento (activo o no), o {@code null}
	 *         si no hay un registro correspondiente.
	 */
	DocumentoEnConsulta findByDocumento(Documento documento);
}
