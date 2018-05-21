package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Adjunto;
import com.laamware.ejercito.doc.web.entity.Documento;
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

    /**
     * Busca un adjunto por su ID y ID de documento, siempre y cuando el
     * registro se encuentre activo.
     *
     * @param id ID del adjunto.
     * @param documento Documento.
     * @return Instancia del adjunto activo correspondiente al ID y al ID del
     * documento, o {@code null} en caso que no exista correspondencia en el
     * sistema o en caso que el registro no esté activo.
     */
    /*
     * 2018-05-21 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162: Proceso de eliminación de adjuntos para registro de actas.
     */
    public Adjunto findByIdAndDocumentoAndActivoTrue(String id, Documento documento);

}
