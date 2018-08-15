package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.dto.NotificacionxUsuarioDTO;
import com.laamware.ejercito.doc.web.entity.NotificacionXUsuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositorio de negocio para {@link NotificacionXUsuario}.
 *
 * @author edison.gonzalez@controltechcg.com
 * @since 1.8
 * @version 25/07/2018 Issue #182 (SICDI-Controltech) feature-182.
 */
public interface NotificacionxUsuarioRepository extends JpaRepository<NotificacionXUsuario, Integer> {

    @Query(value = ""
            + "select TPN.TNF_ID, TPN.NOMBRE, DECODE(NXU.USU_ID, :usuId, 0,1) ACTIVO "
            + "from TIPO_NOTIFICACION TPN "
            + "LEFT JOIN NOTIFICACION_X_USUARIO NXU ON NXU.TNF_ID = TPN.TNF_ID and NXU.USU_ID = :usuId "
            + "where TPN.valor > -2 "
            + "and TPN.activo = 1", nativeQuery = true)
    public List<NotificacionxUsuarioDTO> findNotificacionesXusuario(@Param("usuId") Integer usuID);

    @Query(nativeQuery = true, value = "SELECT * FROM NOTIFICACION_X_USUARIO WHERE USU_ID = :usuId AND TNF_ID = :tnfId")
    NotificacionXUsuario findByUsuIdAndTnfId(@Param("usuId") Integer usuId, @Param("tnfId") Integer tnfId);
    
    @Query(nativeQuery = true, value = "SELECT count(*) FROM NOTIFICACION_X_USUARIO WHERE USU_ID = :usuId AND TNF_ID = :tnfId")
    Integer findCountByUsuIdAndTnfId(@Param("usuId") Integer usuId, @Param("tnfId") Integer tnfId);
}
