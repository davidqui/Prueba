package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.TransJustificacionDefecto;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.TransJustificacionDefectoRepository;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import com.laamware.ejercito.doc.web.util.ReflectionException;
import com.laamware.ejercito.doc.web.util.ReflectionUtil;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Servicio para las justificaciones de las transferencias.
 *
 * @author edisson.gonzalez@controltechcg.com
 * @version 08/22/2018 Issue #4 (SICDI-Controltech) feature-4
 */
@Service
public class TransJustificacionDefectoService {

    private static final Logger LOG = Logger.getLogger(TransJustificacionDefectoService.class.getName());

    /**
     * Repositorio Justificacion de Transferencia por Defecto
     */
    @Autowired
    private TransJustificacionDefectoRepository transJustificacionDefectoRepository;

   
    /**
     * *
     * Lista todas las observaciones
     *
     * @param sort
     * @return
     */
    public List<TransJustificacionDefecto> findAll(Sort sort) {
        return transJustificacionDefectoRepository.findAll(sort);
    }

    /**
     * *
     * Lista todas las observaciones activas
     *
     * @param sort
     * @return
     */
    public List<TransJustificacionDefecto> findActive(Sort sort) {
        return transJustificacionDefectoRepository.getByActivoTrue(sort);
    }

    /**
     * *
     * Busca una justifcacion por defecto por id
     *
     * @param id identificador de la justificacion por defecto
     * @return
     */
    public TransJustificacionDefecto findOne(Long id) {
        return transJustificacionDefectoRepository.getOne(id);
    }

    public void crearTransJustificacionDefecto(TransJustificacionDefecto justificacionDefecto, Usuario usuario) throws BusinessLogicException, ReflectionException {
        final String textoObservacion = justificacionDefecto.getTextoObservacion();
        if (textoObservacion == null || textoObservacion.trim().length() == 0) {
            throw new BusinessLogicException("El texto del nombre es obligatorio.");
        }

        final int textoObservacionColumnLength = ReflectionUtil.getColumnLength(TransJustificacionDefecto.class, "textoObservacion");
        if (textoObservacion.trim().length() > textoObservacionColumnLength) {
            throw new BusinessLogicException("El texto de la observación permite máximo " + textoObservacionColumnLength + " caracteres.");
        }

        TransJustificacionDefecto transJustificacionDefecto = transJustificacionDefectoRepository.findOneByTextoObservacionAndActivoTrue(textoObservacion.toUpperCase());
        System.err.println("transJustificacionDefecto= " + transJustificacionDefecto);
        if (transJustificacionDefecto != null) {
            throw new BusinessLogicException("Este nombre ya existe.");
        }

        justificacionDefecto.setTextoObservacion(textoObservacion.toUpperCase());
        justificacionDefecto.setActivo(Boolean.TRUE);
        justificacionDefecto.setQuien(usuario);
        justificacionDefecto.setCuando(new Date());
        justificacionDefecto.setQuienMod(usuario);
        justificacionDefecto.setCuandoMod(new Date());

        transJustificacionDefectoRepository.saveAndFlush(justificacionDefecto);
    }

    public void editarTransJustificacionDefecto(TransJustificacionDefecto justificacionDefecto, Usuario usuario) throws BusinessLogicException, ReflectionException {

        final String textoObservacion = justificacionDefecto.getTextoObservacion();
        if (textoObservacion == null || textoObservacion.trim().length() == 0) {
            throw new BusinessLogicException("El texto de laobservacion es obligatorio.");
        }

        final int textoObservacionColumnLength = ReflectionUtil.getColumnLength(TransJustificacionDefecto.class, "textoObservacion");
        if (textoObservacion.trim().length() > textoObservacionColumnLength) {
            throw new BusinessLogicException("El texto de la observacion permite máximo " + textoObservacionColumnLength + " caracteres.");
        }

        TransJustificacionDefecto transJustificacionDefecto = transJustificacionDefectoRepository.findOneByTextoObservacionAndActivoTrue(textoObservacion);
        if (transJustificacionDefecto != null && !transJustificacionDefecto.getTjdId().equals(justificacionDefecto.getTjdId()) && transJustificacionDefecto != null) {
            throw new BusinessLogicException("Este texto  ya existe.");
        }

        justificacionDefecto.setTextoObservacion(textoObservacion.toUpperCase());

        TransJustificacionDefecto justificacionDefectoAnterior
                = findOne(justificacionDefecto.getTjdId());

        justificacionDefecto.setQuien(justificacionDefectoAnterior.getQuien());
        justificacionDefecto.setCuando(justificacionDefectoAnterior.getCuando());
        justificacionDefecto.setActivo(justificacionDefectoAnterior.getActivo());

        justificacionDefecto.setQuienMod(usuario);
        justificacionDefecto.setCuandoMod(new Date());

        transJustificacionDefectoRepository.saveAndFlush(justificacionDefecto);
    }

    /**
     * Eliminar una observación por defecto
     *
     * @param transJustificacionDefecto obsevación por defecto a ser eliminada
     * @param usuario Usuario que aplico el cambio
     */
    public void eliminarTransJustificacionDefecto(TransJustificacionDefecto transJustificacionDefecto,
        Usuario usuario) {
        transJustificacionDefecto.setQuienMod(usuario);
        transJustificacionDefecto.setCuandoMod(new Date());
        transJustificacionDefecto.setActivo(Boolean.FALSE);
        transJustificacionDefectoRepository.saveAndFlush(transJustificacionDefecto);
    }

    /**
     * Lista todas las observaciones por defecto activas, ordenadas por el
     * texto.
     *
     * @return Lista de observaciones activas ordenadas por texto.
     */
    public List<TransJustificacionDefecto> listarActivas() {
        return transJustificacionDefectoRepository.findAllByActivoTrueOrderByTextoObservacionAsc();
    }

}
