package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Adjunto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdjuntoRepository extends JpaRepository<Adjunto, String> {

    List<Adjunto> getAdjuntosByDocumentoId(String docId);

    /**
     * Busca todos los documentos adjuntos activos para un documento.
     *
     * @param docId ID del documento.
     * @return Lista de documentos adjuntos activos.
     */
    /*
     * 2018-04-13 jgarcia@contrltechcg.com Issue #156 (SICDI-Controltech)
     * feature-156.
     */
    public List<Adjunto> findAllByDocumentoIdAndActivoTrue(String docId);

}
