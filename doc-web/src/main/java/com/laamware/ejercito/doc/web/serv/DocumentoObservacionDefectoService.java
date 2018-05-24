package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.DocumentoObservacionDefecto;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.DocumentoObservacionDefectoRepository;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    private static final Logger LOG = Logger.getLogger(DocumentoObservacionDefectoService.class.getName());

    @Autowired
    private DocumentoObservacionDefectoRepository documentoObservacionDefectoRepository;

    public List<DocumentoObservacionDefecto> findAll(Sort sort) {
        return documentoObservacionDefectoRepository.findAll(sort);
    }

    public List<DocumentoObservacionDefecto> findActive(Sort sort) {
        return documentoObservacionDefectoRepository.getByActivoTrue(sort);
    }

    public DocumentoObservacionDefecto findOne(Integer id) {
        return documentoObservacionDefectoRepository.findOne(id);
    }

    /**
     * Creación de una obsevación por defecto
     *
     * @param documentoObservacionDefecto dominio a ser creado
     * @param usuario Usuario que aplico el cambio
     * @return
     */
    public String crearObservacionDefecto(DocumentoObservacionDefecto documentoObservacionDefecto,
            Usuario usuario) {
        String mensaje = "OK";
        try {
            System.err.println("documentoobservacionservice= " + documentoObservacionDefecto);
            if (documentoObservacionDefecto.getTextoObservacion() == null) {
                if (documentoObservacionDefecto.getTextoObservacion().trim().length() == 0) {
                    return "Error-El texto de la observación es obligatorio.";
                }
                if (documentoObservacionDefecto.getTextoObservacion().trim().length() > 64) {
                    return "Error-El texto de la observación está restringido a 64 caracteres.";
                }
            }
            documentoObservacionDefecto.setQuien(usuario);
            documentoObservacionDefecto.setCuando(new Date());
            documentoObservacionDefecto.setActivo(Boolean.TRUE);
            documentoObservacionDefecto.setQuienMod(usuario);
            documentoObservacionDefecto.setCuandoMod(new Date());

            documentoObservacionDefectoRepository.saveAndFlush(documentoObservacionDefecto);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            mensaje = "Excepcion-" + ex.getMessage();
        }
        return mensaje;
    }

    /**
     * Editar una obsevación por defecto
     *
     * @param documentoObservacionDefecto obsevación por defecto a ser editada
     * @param usuario Usuario que aplico el cambio
     * @return
     */
    public String editarObservacionDefecto(DocumentoObservacionDefecto documentoObservacionDefecto,
            Usuario usuario) {
        String mensaje = "OK";
        try {
            System.err.println("documentoobservacionservice= " + documentoObservacionDefecto);
            if (documentoObservacionDefecto.getTextoObservacion() == null) {
                if (documentoObservacionDefecto.getTextoObservacion().trim().length() == 0) {
                    return "Error-El texto de la observación es obligatorio.";
                }
                if (documentoObservacionDefecto.getTextoObservacion().trim().length() > 64) {
                    return "Error-El texto de la observación está restringido a 64 caracteres.";
                }
            }

            DocumentoObservacionDefecto documentoObservacionAnterior
                    = findOne(documentoObservacionDefecto.getId());

            documentoObservacionDefecto.setQuien(documentoObservacionAnterior.getQuien());
            documentoObservacionDefecto.setCuando(documentoObservacionAnterior.getCuando());
            documentoObservacionDefecto.setActivo(documentoObservacionAnterior.getActivo());

            documentoObservacionDefecto.setQuienMod(usuario);
            documentoObservacionDefecto.setCuandoMod(new Date());

            documentoObservacionDefectoRepository.saveAndFlush(documentoObservacionDefecto);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            mensaje = "Excepcion-" + ex.getMessage();
        }
        return mensaje;
    }

    /**
     * Eliminar una observación por defecto
     *
     * @param documentoObservacionDefecto obsevación por defecto a ser eliminada
     * @param usuario Usuario que aplico el cambio
     * @return
     */
    public String eliminarObservacionDefecto(DocumentoObservacionDefecto documentoObservacionDefecto,
            Usuario usuario) {
        String mensaje = "OK";
        try {
            documentoObservacionDefecto.setQuien(usuario);
            documentoObservacionDefecto.setCuando(new Date());

            documentoObservacionDefecto.setActivo(Boolean.FALSE);

            documentoObservacionDefectoRepository.saveAndFlush(documentoObservacionDefecto);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            mensaje = "Excepcion-" + ex.getMessage();
        }
        return mensaje;
    }

    /**
     * Lista todas las observaciones por defecto activas, ordenadas por el
     * texto.
     *
     * @return Lista de observaciones activas ordenadas por texto.
     */
    public List<DocumentoObservacionDefecto> listarActivas() {
        return documentoObservacionDefectoRepository.findAllByActivoTrueOrderByTextoObservacionAsc();
    }

}
