package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Adjunto;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.repo.AdjuntoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio de lógica de negocio para {@link Adjunto}
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 04/13/2018 (SICDI-Controltech Issue #156 feature-156)
 */
@Service
public class AdjuntoService {

    @Autowired
    private AdjuntoRepository adjuntoRepository;

    /**
     * Lista todos los adjuntos activos de un documento.
     *
     * @param documento Documento.
     * @return Lista de todos los adjuntos activos del documento.
     */
    public List<Adjunto> findAllActivos(final Documento documento) {
        return adjuntoRepository.findAllByDocumentoIdAndActivoTrue(documento.getId());
    }
}
