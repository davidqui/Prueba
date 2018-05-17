package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.DocumentoObservacion;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.DocumentoObservacionRepository;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio de lógica de negocio para {@link DocumentoObservacion}.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/17/2018 Issue #162 (SICDI-Controltech) feature-162.
 */
@Service
public class DocumentoObservacionService {

    @Autowired
    private DocumentoObservacionRepository observacionRepository;

    /**
     * Crea una observación para un documento.
     *
     * @param documento Documento.
     * @param texto Texto de la observación.
     * @param usuario Usuario que registra el comentario.
     * @return Instancia de la observación del documento creada.
     */
    public DocumentoObservacion crearObservacion(final Documento documento, final String texto, final Usuario usuario) {
        DocumentoObservacion observacion = new DocumentoObservacion();
        observacion.setCuando(new Date());
        observacion.setDocumento(documento);
        observacion.setQuien(usuario.getId());
        observacion.setTexto(texto);

        return observacionRepository.saveAndFlush(observacion);
    }

}
