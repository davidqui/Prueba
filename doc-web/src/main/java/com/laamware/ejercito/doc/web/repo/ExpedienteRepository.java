package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Expediente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExpedienteRepository extends JpaRepository<Expediente, Long> {

    String CONSULTALISTAEXPEDIENTESXUSUARIO =""
            + "SELECT EXP.* "
            + "FROM EXPEDIENTE EXP "
            + "LEFT OUTER JOIN EXP_USUARIO EXPUSUARIO ON (EXPUSUARIO.EXP_ID = EXP.EXP_ID) " 
            + "WHERE (EXP.USU_CREACION = :usuId OR (EXPUSUARIO.USU_ID = :usuId and EXPUSUARIO.IND_APROBADO = 1 AND EXPUSUARIO.ACTIVO = 1))";
    
     /**
     * Obtiene el numero de registros de expedientes por usuario.
     *
     * @param usuId Identificador del Usuario
     * @return Numero de registros.
     */
    @Query(value = "select count(1)\n"
            + "from(\n"
            + CONSULTALISTAEXPEDIENTESXUSUARIO
            + ") exp\n", nativeQuery = true)
    int findExpedientesPorUsuarioCount(@Param("usuId") Integer usuId);
    
    /**
     * Obtiene los registros de expedientes por usuario, de acuerdo
     * a la fila inicial y final.
     *
     * @param usuId Identificador del usuario
     * @param inicio Número de registro inicial
     * @param fin Numero de registro final
     * @return Lista de bandejas de entrada.
     */
    @Query(value = ""
            + "select exp.*\n"
            + "from(\n"
            + "     select exp.*, rownum num_lineas\n"
            + "     from(\n"
            + CONSULTALISTAEXPEDIENTESXUSUARIO
            + "         ORDER BY exp.fec_modificacion DESC\n"
            + "     )exp\n"
            + ") exp\n"
            + "where exp.num_lineas >= :inicio and exp.num_lineas <= :fin\n", nativeQuery = true)
    List<Expediente> findExpedientesPorUsuarioPaginado(@Param("usuId") Integer usuId, @Param("inicio") int inicio, @Param("fin") int fin);
}
