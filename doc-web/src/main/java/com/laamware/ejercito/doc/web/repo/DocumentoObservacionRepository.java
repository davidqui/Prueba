package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.DocumentoObservacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoObservacionRepository extends JpaRepository<DocumentoObservacion, Integer> {

    /**
     * Busca todos las observaciones para un documento ordenadas
     * descendentemente por fecha de creación.
     *
     * @param documento Documento.
     * @return Lista de observaciones del documento ordenadas de forma
     * descendente por fecha de creación.
     */
    /*
     * 2018-05-17 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162.
     */
    public List<DocumentoObservacion> findAllByDocumentoOrderByCuandoDesc(Documento documento);

}
