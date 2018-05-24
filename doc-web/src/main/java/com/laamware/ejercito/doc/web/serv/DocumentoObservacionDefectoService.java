package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.DocumentoObservacionDefecto;
import com.laamware.ejercito.doc.web.repo.DocumentoObservacionDefectoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio de lógica de negocio para {@link DocumentoObservacionDefecto}.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/23/2018 Issue #172 (SICDI-Controltech) feature-172
 */
@Service
public class DocumentoObservacionDefectoService {

    @Autowired
    private DocumentoObservacionDefectoRepository observacionDefectoRepository;

    /**
     * Lista todas las observaciones por defecto activas, ordenadas por el
     * texto.
     *
     * @return Lista de observaciones activas ordenadas por texto.
     */
    public List<DocumentoObservacionDefecto> listarActivas() {
        return observacionDefectoRepository.findAllByActivoTrueOrderByTextoObservacionAsc();
    }

}
