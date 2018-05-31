package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.TipoNotificacion;
import com.laamware.ejercito.doc.web.repo.TipoNotificacionRepository;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Servicio de lógica de negocio para {@link TipoNotificacion}.
 *
 * @author samuel.delgado@controltechcg.com 
 * @since 1.8
 * @version 30/05/2018 Issue #169 (SICDI-Controltech) feature-169
 */
@Service
public class TipoNotificacionService {
    
    private static final Logger LOG = Logger.getLogger(TipoNotificacionService.class.getName());
    
    @Autowired
    private TipoNotificacionRepository tipoNotificacionRepository;
    
    /**
     * *
     * Lista todas los Tipo de notificación
     *
     * @param sort
     * @return
     */
    public List<TipoNotificacion> findAll(Sort sort) {
        return tipoNotificacionRepository.findAll(sort);
    }

    /**
     * *
     * Lista todas los Tipo de notificación activas
     *
     * @param sort
     * @return
     */
    public List<TipoNotificacion> findActive(Sort sort) {
        return tipoNotificacionRepository.getByActivoTrue(sort);
    }

    /**
     * *
     * Busca un Tipo de notificación por defecto por id
     *
     * @param id identificador de la observacion por defecto
     * @return
     */
    public TipoNotificacion findOne(Integer id) {
        return tipoNotificacionRepository.findOne(id);
    }
}
