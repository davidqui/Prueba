package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.TipoNotificacion;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de persistencia para {@link WildCardNotificacion}.
 *
 * @author samuel.delgado@controltechcg.com 
 * @since 1.8
 * @version 30/05/2018 Issue #169 (SICDI-Controltech) feature-169
 */
@Repository
public interface TipoNotificacionRepository extends JpaRepository<TipoNotificacion, Integer>{
    
     /**
     * Lista todas los tipos de notificación activas
     * @param sort
     * @return 
     */
    List<TipoNotificacion> getByActivoTrue(Sort sort);
}
