package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.TransferenciaArchivo;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositorio de persistencia para {@link TransferenciaArchivo}.
 *
 * @author jgarcia@controltechcg.com
 * @since Ago 25, 2017
 * @version 1.0.0 (feature-120).
 */
public interface TransferenciaArchivoRepository extends GenJpaRepository<TransferenciaArchivo, Integer> {

    /**
     * Obtiene todas las transferencias de archivo activas en estado
     * {@link TransferenciaArchivo#APROBADA_ESTADO} para un usuario destino,
     * ordenadas por la fecha de aprobación.
     *
     * @param destinoUsuario ID del usuario destino.
     * @return Lista de transferencias.
     */
    @Query(nativeQuery = true, value = ""
            + "SELECT \n"
            + "    * \n"
            + "FROM \n"
            + "    TRANSFERENCIA_ARCHIVO \n"
            + "WHERE \n"
            + "        ACTIVO = 1 \n"
            + "    AND \n"
            + "        ESTADO = 'A' \n"
            + "    AND \n"
            + "        DESTINO_USU_ID =:destinoUsuario \n"
            + "ORDER BY FECHA_APROBACION DESC"
            + "")
    public List<TransferenciaArchivo> findAllRecibidasActivasByDestinoUsuario(
            @Param("destinoUsuario") Integer destinoUsuario);

    /**
     * Consulta que se encarga de listar las transferencias por un usuario
     */
    String CONSULTALISTATRANSFERENCIAARCHIVO = ""
            + "select TA.tar_id,\n"
            + "       TA.activo,\n"
            + "       TA.estado,\n"
            + "       TA.fecha_creacion,\n"
            + "       TA.origen_usu_id,\n"
            + "       USUARIO_ORI.USU_NOMBRE USU_ORIGEN,\n"
            + "       TA.usu_origen_cargo,\n"
            + "       CARORI.CAR_NOMBRE CAR_USU_ORIGEN,\n"
            + "       TA.DESTINO_USU_ID,\n"
            + "       USUARIO_DES.USU_NOMBRE USU_DESTINO,\n"
            + "       TA.usu_DESTINO_cargo,\n"
            + "       CARDES.CAR_NOMBRE CAR_USU_DESTINO,\n"
            + "       TA.ORIGEN_DEP_ID,\n"
            + "       DEP.USU_ID_JEFE,\n"
            + "       TA.JUSTIFICACION,\n"
            + "       TA.USUARIO_ASIGNADO,\n"
            + "       TA.IND_APROBADO,\n"
            + "       (SELECT COUNT(1)\n"
            + "        FROM TRANSFERENCIA_ARCHIVO_DETALLE TAD\n"
            + "        WHERE TAD.TAR_ID = TA.TAR_ID\n"
            + "        AND TAD.ACTIVO = 1\n"
            + "        AND TAD.IND_REALIZADO = 0) NUM_DOCUMENTOS,\n"
            + "       (SELECT COUNT(1)\n"
            + "        FROM TRANS_EXPEDIENTE_DETALLE TED\n"
            + "        WHERE TED.TAR_ID = TA.TAR_ID\n"
            + "        AND TED.ACTIVO = 1\n"
            + "        AND TED.IND_REALIZADO = 0) NUM_EXPEDIENTES,\n"
            + "       (SELECT TE.TRA_EST_NOMBRE\n"
            + "        FROM TRANSFERENCIA_TRANSICION TT,\n"
            + "             TRANSFERENCIA_ESTADO TE\n"
            + "        WHERE TE.TRA_EST_ID = TT.TRA_EST_ID\n"
            + "        AND TT.TAR_ID = TA.TAR_ID\n"
            + "        AND TT.FEC_CREACION = (SELECT MAX(FEC_CREACION) FROM TRANSFERENCIA_TRANSICION WHERE TT.TAR_ID = TA.TAR_ID)) ULT_ESTADO,\n"
            + "        decode(:usuId,TA.ORIGEN_USU_ID, 1, 0) ESUSUORIGEN,\n"
            + "        decode(:usuId,DEP.USU_ID_JEFE, 1, 0) ESJEFE,\n"
            + "        decode(:usuId,TA.DESTINO_USU_ID, 1, 0) ESUSUDESTINO\n"
            + "from TRANSFERENCIA_ARCHIVO TA\n"
            + "JOIN CARGO CARORI ON (CARORI.CAR_ID = TA.USU_ORIGEN_CARGO)\n"
            + "LEFT JOIN CARGO CARDES ON (CARDES.CAR_ID = TA.DESTINO_USU_ID)\n"
            + "JOIN USUARIO USUARIO_DES ON (USUARIO_DES.USU_ID = TA.DESTINO_USU_ID)\n"
            + "JOIN USUARIO USUARIO_ORI ON (USUARIO_ORI.USU_ID = TA.ORIGEN_USU_ID)\n"
            + "JOIN DEPENDENCIA DEP ON (DEP.DEP_ID = TA.ORIGEN_DEP_ID)\n"
            + "WHERE TA.NUM_DOCUMENTOS is null\n"
            + "AND TA.ESTADO != 'C'\n";

    /**
     * Obtiene el numero de registros de transferencias en proceso por usuario.
     *
     * @param usuId Identificador del Usuario
     * @return Numero de registros.
     */
    @Query(value = "select count(1)\n"
            + "from(\n"
            + CONSULTALISTATRANSFERENCIAARCHIVO
            + "AND ((ORIGEN_USU_ID = :usuId) OR (DEP.USU_ID_JEFE = :usuId AND TA.USUARIO_ASIGNADO = 2) OR (TA.DESTINO_USU_ID = :usuId AND TA.USUARIO_ASIGNADO = 1))\n"
            + "AND TA.IND_APROBADO = 0\n"
            + ") ta\n", nativeQuery = true)
    int findCountProcesoByUsuarioId(@Param("usuId") Integer usuId);

    /**
     * Obtiene los registros dde transferencias en proceso por usuario, de
     * acuerdo a la fila inicial y final.
     *
     * @param usuId Identificador del usuario
     * @param inicio Número de registro inicial
     * @param fin Numero de registro final
     * @return Lista de bandejas de entrada.
     */
    @Query(value = ""
            + "select ta.*\n"
            + "from(\n"
            + "     select ta.*, rownum num_lineas\n"
            + "     from(\n"
            + CONSULTALISTATRANSFERENCIAARCHIVO
            + "     AND ((ORIGEN_USU_ID = :usuId) OR (DEP.USU_ID_JEFE = :usuId AND TA.USUARIO_ASIGNADO = 2) OR (TA.DESTINO_USU_ID = :usuId AND TA.USUARIO_ASIGNADO = 1))\n"
            + "     AND TA.IND_APROBADO = 0\n"
            + "     ORDER BY FECHA_CREACION desc\n"
            + "     )ta\n"
            + ") ta\n"
            + "where ta.num_lineas >= :inicio and ta.num_lineas <= :fin\n", nativeQuery = true)
    List<Object[]> findAllProcesoByUsuarioId(@Param("usuId") Integer usuId, @Param("inicio") int inicio, @Param("fin") int fin);

    /**
     * Obtiene el numero de registros de transferencias realizadas por usuario.
     *
     * @param usuId Identificador del Usuario
     * @return Numero de registros.
     */
    @Query(value = "select count(1)\n"
            + "from(\n"
            + CONSULTALISTATRANSFERENCIAARCHIVO
            + "AND ORIGEN_USU_ID = :usuId\n"
            + "AND TA.IND_APROBADO = 1\n"
            + ") ta\n", nativeQuery = true)
    int findCountRealizadasByUsuarioId(@Param("usuId") Integer usuId);

    /**
     * Obtiene los registros dde transferencias realizadas por usuario, de acuerdo
     * a la fila inicial y final.
     *
     * @param usuId Identificador del usuario
     * @param inicio Número de registro inicial
     * @param fin Numero de registro final
     * @return Lista de bandejas de entrada.
     */
    @Query(value = ""
            + "select ta.*\n"
            + "from(\n"
            + "     select ta.*, rownum num_lineas\n"
            + "     from(\n"
            + CONSULTALISTATRANSFERENCIAARCHIVO
            + "     AND ORIGEN_USU_ID = :usuId\n"
            + "     AND TA.IND_APROBADO = 1\n"
            + "     ORDER BY FECHA_CREACION desc\n"
            + "     )ta\n"
            + ") ta\n"
            + "where ta.num_lineas >= :inicio and ta.num_lineas <= :fin\n", nativeQuery = true)
    List<Object[]> findAllRealizadasByUsuarioId(@Param("usuId") Integer usuId, @Param("inicio") int inicio, @Param("fin") int fin);
    
    /**
     * Obtiene el numero de registros de transferencias recibidas por usuario.
     *
     * @param usuId Identificador del Usuario
     * @return Numero de registros.
     */
    @Query(value = "select count(1)\n"
            + "from(\n"
            + CONSULTALISTATRANSFERENCIAARCHIVO
            + "AND DESTINO_USU_ID = :usuId\n"
            + "AND TA.IND_APROBADO = 1\n"
            + ") ta\n", nativeQuery = true)
    int findCountRecibidasByUsuarioId(@Param("usuId") Integer usuId);

    /**
     * Obtiene los registros dde transferencias recibidas por usuario, de acuerdo
     * a la fila inicial y final.
     *
     * @param usuId Identificador del usuario
     * @param inicio Número de registro inicial
     * @param fin Numero de registro final
     * @return Lista de bandejas de entrada.
     */
    @Query(value = ""
            + "select ta.*\n"
            + "from(\n"
            + "     select ta.*, rownum num_lineas\n"
            + "     from(\n"
            + CONSULTALISTATRANSFERENCIAARCHIVO
            + "     AND DESTINO_USU_ID = :usuId\n"
            + "     AND TA.IND_APROBADO = 1\n"
            + "     ORDER BY FECHA_CREACION desc\n"
            + "     )ta\n"
            + ") ta\n"
            + "where ta.num_lineas >= :inicio and ta.num_lineas <= :fin\n", nativeQuery = true)
    List<Object[]> findAllRecibidasByUsuarioId(@Param("usuId") Integer usuId, @Param("inicio") int inicio, @Param("fin") int fin);
}
