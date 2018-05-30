package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Notificacion;
import com.laamware.ejercito.doc.web.repo.NotificacionRepository;
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
}
