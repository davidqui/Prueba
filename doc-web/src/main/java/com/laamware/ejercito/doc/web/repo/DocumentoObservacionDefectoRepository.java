package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.DocumentoObservacionDefecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de persistencia para {@link DocumentoObservacionDefecto}.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/23/2018 Issue #172 (SICDI-Controltech) feature-172
 */
@Repository
public interface DocumentoObservacionDefectoRepository extends JpaRepository<DocumentoObservacionDefecto, Integer> {

}
