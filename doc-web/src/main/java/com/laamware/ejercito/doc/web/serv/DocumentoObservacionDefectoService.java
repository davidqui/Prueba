package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.DocumentoObservacionDefecto;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.DocumentoObservacionDefectoRepository;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import com.laamware.ejercito.doc.web.util.ReflectionException;
import com.laamware.ejercito.doc.web.util.ReflectionUtil;
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

    /**
     * *
     * Lista todas las observaciones
     *
     * @param sort
     * @return
     */
    public List<DocumentoObservacionDefecto> findAll(Sort sort) {
        return documentoObservacionDefectoRepository.findAll(sort);
    }

    /**
     * *
     * Lista todas las observaciones activas
     *
     * @param sort
     * @return
     */
    public List<DocumentoObservacionDefecto> findActive(Sort sort) {
        return documentoObservacionDefectoRepository.getByActivoTrue(sort);
    }

    /**
     * *
     * Busca una observacion por defecto por id
     *
     * @param id identificador de la observacion por defecto
     * @return
     */
    public DocumentoObservacionDefecto findOne(Integer id) {
        return documentoObservacionDefectoRepository.findOne(id);
    }

    /**
     * Creación de una obsevación por defecto
     *
     * @param documentoObservacionDefecto observación por defecto a ser creada
     * @param usuario Usuario que aplico el cambio
     * @throws com.laamware.ejercito.doc.web.util.BusinessLogicException En caso
     * que no se cumplan las validaciones de negocio.
     * @throws com.laamware.ejercito.doc.web.util.ReflectionException En caso
     * que se presenten errores con funciones de relection.
     */
    public void crearObservacionDefecto(DocumentoObservacionDefecto documentoObservacionDefecto, Usuario usuario) throws BusinessLogicException, ReflectionException {
        final String textoObservacion = documentoObservacionDefecto.getTextoObservacion();
        if (textoObservacion == null || textoObservacion.trim().length() == 0) {
            throw new BusinessLogicException("El texto de la observación es obligatorio.");
        }

        final int textoObservacionColumnLength = ReflectionUtil.getColumnLength(DocumentoObservacionDefecto.class, "textoObservacion");
        if (textoObservacion.trim().length() > textoObservacionColumnLength) {
            throw new BusinessLogicException("El texto de la observación permite máximo " + textoObservacionColumnLength + " caracteres.");
        }
        
        final DocumentoObservacionDefecto anterior = documentoObservacionDefectoRepository.getOneByActivoTrueAndTextoObservacion(textoObservacion);
        if (anterior != null) {
            throw new BusinessLogicException("Este nombre ya existe.");
        }
        
        documentoObservacionDefecto.setQuien(usuario);
        documentoObservacionDefecto.setCuando(new Date());
        documentoObservacionDefecto.setActivo(Boolean.TRUE);
        documentoObservacionDefecto.setQuienMod(usuario);
        documentoObservacionDefecto.setCuandoMod(new Date());

        documentoObservacionDefectoRepository.saveAndFlush(documentoObservacionDefecto);
    }

    /**
     * Editar una obsevación por defecto
     *
     * @param documentoObservacionDefecto obsevación por defecto a ser editada
     * @param usuario Usuario que aplico el cambio
     * @throws com.laamware.ejercito.doc.web.util.ReflectionException En caso
     * que se presenten errores con funciones de relection.
     */
    public void editarObservacionDefecto(DocumentoObservacionDefecto documentoObservacionDefecto, Usuario usuario) throws BusinessLogicException, ReflectionException{
        
            final String textoObservacion = documentoObservacionDefecto.getTextoObservacion();
            if (textoObservacion == null || textoObservacion.trim().length() == 0) {
                throw new BusinessLogicException("El texto de la observación es obligatorio.");
            }

            final int textoObservacionColumnLength = ReflectionUtil.getColumnLength(DocumentoObservacionDefecto.class, "textoObservacion");
            if (textoObservacion.trim().length() > textoObservacionColumnLength) {
                throw new BusinessLogicException("El texto de la observación permite máximo " + textoObservacionColumnLength + " caracteres.");
            }

            final DocumentoObservacionDefecto anterior = documentoObservacionDefectoRepository.getOneByActivoTrueAndTextoObservacion(textoObservacion);
            if (anterior != null && !anterior.getId().equals(documentoObservacionDefecto.getId())) {
                throw new BusinessLogicException("Este nombre ya existe.");
            }
        
            DocumentoObservacionDefecto documentoObservacionAnterior
                    = findOne(documentoObservacionDefecto.getId());

            documentoObservacionDefecto.setQuien(documentoObservacionAnterior.getQuien());
            documentoObservacionDefecto.setCuando(documentoObservacionAnterior.getCuando());
            documentoObservacionDefecto.setActivo(documentoObservacionAnterior.getActivo());

            documentoObservacionDefecto.setQuienMod(usuario);
            documentoObservacionDefecto.setCuandoMod(new Date());

            documentoObservacionDefectoRepository.saveAndFlush(documentoObservacionDefecto);
    }

    /**
     * Eliminar una observación por defecto
     *
     * @param documentoObservacionDefecto obsevación por defecto a ser eliminada
     * @param usuario Usuario que aplico el cambio
     */
    public void eliminarObservacionDefecto(DocumentoObservacionDefecto documentoObservacionDefecto,
            Usuario usuario) {
        documentoObservacionDefecto.setQuienMod(usuario);
        documentoObservacionDefecto.setCuandoMod(new Date());
        documentoObservacionDefecto.setActivo(Boolean.FALSE);
        documentoObservacionDefectoRepository.saveAndFlush(documentoObservacionDefecto);
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
