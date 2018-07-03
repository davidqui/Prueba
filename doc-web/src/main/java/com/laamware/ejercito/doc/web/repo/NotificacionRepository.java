package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Notificacion;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de persistencia para {@link WildCardNotificacion}.
 *
 * @author samuel.delgado@controltechcg.com 
 * @since 1.8
 * @version 30/05/2018 Issue #169 (SICDI-Controltech) feature-169
 */
@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer>{
    
    /**
     * Lista todas las notificaciones activas
     * @param sort
     * @return 
     */
    List<Notificacion> getByActivoTrue(Sort sort);
    
    @Query(nativeQuery = true, value = ""
            + "SELECT NOTIFICACION.*                                                                        "
            + "FROM NOTIFICACION JOIN TIPO_NOTIFICACION ON (NOTIFICACION.TNF_ID = TIPO_NOTIFICACION.TNF_ID) "
            + "WHERE TIPO_NOTIFICACION.VALOR = :tipoNotValue AND NOTIFICACION.ACTIVO = 1                    ")
    List<Notificacion> getByValorTipoNotificacion(@Param("tipoNotValue") Integer tipoNotValue);
    
    @Query(nativeQuery = true, value = ""
            + "SELECT NOTIFICACION.*                                                                        "
            + "FROM NOTIFICACION JOIN TIPO_NOTIFICACION ON (NOTIFICACION.TNF_ID = TIPO_NOTIFICACION.TNF_ID) "
            + "WHERE TIPO_NOTIFICACION.TNF_ID = :tipoNotId AND NOTIFICACION.ACTIVO = 1                      ")
    List<Notificacion> getByIdTipoNotificacion(@Param("tipoNotId") Integer tipoNotId);
}
