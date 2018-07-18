package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.WildCardNotificacion;
import com.laamware.ejercito.doc.web.repo.WildCardNotificacionRepository;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Servicio de lógica de negocio para {@link WildCardNotificacion}.
 *
 * @author samuel.delgado@controltechcg.com 
 * @since 1.8
 * @version 30/05/2018 Issue #169 (SICDI-Controltech) feature-169
 */
@Service
public class WildCardNotificacionService {
    
    private static final Logger LOG = Logger.getLogger(WildCardNotificacionService.class.getName());
    
    @Autowired
    private WildCardNotificacionRepository wildCardNotificationRepository;

    /**
     * *
     * Lista todas los elementos del template de notificacion
     *
     * @param sort
     * @return
     */
    public List<WildCardNotificacion> findAll(Sort sort) {
        return wildCardNotificationRepository.findAll(sort);
    }
}
