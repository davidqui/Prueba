package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Notificacion;
import com.laamware.ejercito.doc.web.entity.TipoNotificacion;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.NotificacionRepository;
import com.laamware.ejercito.doc.web.util.BusinessLogicException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Servicio de lógica de negocio para {@link Notificacion}.
 *
 * @author samuel.delgado@controltechcg.com 
 * @since 1.8
 * @version 30/05/2018 Issue #169 (SICDI-Controltech) feature-169
 */
@Service
public class NotificacionService {
    
    private static final Logger LOG = Logger.getLogger(NotificacionService.class.getName());
    
    @Autowired
    private NotificacionRepository notificacionRepository;
    
    /**
     * *
     * Lista todas las observaciones
     *
     * @param sort
     * @return
     */
    public List<Notificacion> findAll(Sort sort) {
        return notificacionRepository.findAll(sort);
    }

    /**
     * *
     * Lista todas las observaciones activas
     *
     * @param sort
     * @return
     */
    public List<Notificacion> findActive(Sort sort) {
        return notificacionRepository.getByActivoTrue(sort);
    }

    /**
     * *
     * Busca una observacion por defecto por id
     *
     * @param id identificador de la observacion por defecto
     * @return
     */
    public Notificacion findOne(Integer id) {
        return notificacionRepository.findOne(id);
    }
    
    /***
     * Creación de una notificación
     * @param notificacion notificación a agregar
     * @param usuario usuario que realiza la acción
     * @throws BusinessLogicException
     */
    public void crearNotificacion(Notificacion notificacion, Usuario usuario) throws BusinessLogicException {
        final TipoNotificacion tipoNotificacion = notificacion.getTipoNotificacion();
        if (tipoNotificacion == null) {
            throw new BusinessLogicException("Debe seleccionar un tipo de notificación");
        }
        final String template = notificacion.getTemplate();
        if (template.trim().length() < 1) {
            throw new BusinessLogicException("El texto del template es un campo obligatorio.");
        }
        
        notificacion.setQuien(usuario);
        notificacion.setCuando(new Date());
        notificacion.setActivo(Boolean.TRUE);
        notificacion.setQuienMod(usuario);
        notificacion.setCuandoMod(new Date());
        
        notificacionRepository.saveAndFlush(notificacion);
    }
    
    /***
     * Edición de una notificación
     * @param notificacion notificación a modificar
     * @param usuario Usuario que modificó
     * @throws BusinessLogicException 
     */
    public void editarNotifiacion(Notificacion notificacion, Usuario usuario) throws BusinessLogicException {
        final TipoNotificacion tipoNotificacion = notificacion.getTipoNotificacion();
        if (tipoNotificacion == null) {
            throw new BusinessLogicException("Debe seleccionar un tipo de notificación");
        }
        final String template = notificacion.getTemplate();
        if (template.trim().length() < 1) {
            throw new BusinessLogicException("El texto del template es un campo obligatorio.");
        }
        
        Notificacion notificacionAnterior = notificacionRepository.findOne(notificacion.getId());
        System.out.println("Que suceded? "+ notificacionAnterior.toString());
        notificacion.setQuien(notificacionAnterior.getQuien());
        notificacion.setCuando(notificacionAnterior.getCuando());
        notificacion.setActivo(notificacionAnterior.getActivo());
        
        notificacion.setQuienMod(usuario);
        notificacion.setCuandoMod(new Date());

        notificacionRepository.saveAndFlush(notificacion);
    }
    
    /**
     * Eliminación de una notificación
     * @param notificacion notificación a eliminar
     * @param usuario Usuario que elimino
     */
    public void eliminarNotifiacion(Notificacion notificacion, Usuario usuario){
        notificacion.setQuien(usuario);
        notificacion.setCuando(new Date());
        notificacion.setActivo(Boolean.FALSE);
        notificacionRepository.saveAndFlush(notificacion);
    }
}
