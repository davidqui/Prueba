package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.Expediente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExpedienteRepository extends JpaRepository<Expediente, Long> {

    /**
     * Trae una lista de expedientes dado un nombre y una dependecia.
     * @param nombre
     * @param dependencia
     * @return
     */
    List<Expediente> getByExpNombreAndDepId(String nombre, Dependencia dependencia);

    /**
     * Consulta que se encarga de listar los expedientes por usuario
     */
    String CONSULTALISTAEXPEDIENTESXUSUARIO = ""
            + "SELECT DISTINCT EXP.EXP_ID,  "
            + "       EXP.EXP_NOMBRE, "
            + "       EXP.FEC_CREACION, "
            + "       DEP_JEFE.DEP_ID, "
            + "       DEP_JEFE.DEP_NOMBRE, "
            + "       TRDPRINCIPAL.TRD_ID, "
            + "       TRDPRINCIPAL.TRD_CODIGO||' '||TRDPRINCIPAL.TRD_NOMBRE, "
            + "       DECODE(DEP_JEFE.USU_ID_JEFE,:usuId,1,0) indjefedependencia, "
            + "       DECODE(EXP.USU_CREACION, :usuId, 1, 0) indusucreador, "
            + "       IND_APROBADO_INICIAL IND_APROBADO_INICIAL, "
            + "       UJD.USU_GRADO||' '||UJD.USU_NOMBRE JEFEDEPENDENCIA, "
            + "       UC.USU_GRADO||' '||UC.USU_NOMBRE USUARIOCREADOR, "
            + "       EXP.EXP_TIPO, "
            + "       DECODE(EXP.EXP_TIPO, 1, 'EXPEDIENTE SIMPLE', 'EXPEDIENTE COMPLEJO') expTipo, "
            + "       EXP.EXP_DESCRIPCION, "
            + "       NVL((SELECT case when count(1) > 0 then 1 else 0 end "
            + "             FROM EXP_USUARIO Z "
            + "             WHERE Z.EXP_ID = EXP.EXP_ID"
            + "             and Z.IND_APROBADO = 0 "
            + "             AND Z.ACTIVO = 1),0) INDUSUARIOASIGNADO, "
            + "       EXP.IND_CERRADO, "
            + "       (SELECT COUNT(1) "
            + "        FROM EXP_TRD EXPTRD "
            + "        WHERE EXPTRD.EXP_ID = EXP.EXP_ID "
            + "        AND EXPTRD.ACTIVO      = 1) numTrdComplejo, "
            + "       (SELECT COUNT(1)"
            + "        FROM EXP_USUARIO EXPUSUARIO "
            + "        WHERE EXPUSUARIO.EXP_ID = EXP.EXP_ID "
            + "        AND EXPUSUARIO.IND_APROBADO = 1 "
            + "        AND EXPUSUARIO.ACTIVO      = 1) numUsuarios, "
            + "       (SELECT COUNT(1) "
            + "        FROM EXP_DOCUMENTO EXPDOCUMENTO "
            + "        WHERE EXPDOCUMENTO.EXP_ID = EXP.EXP_ID"
            + "        AND EXPDOCUMENTO.ACTIVO = 1) numDocumentos, "
            + "        NVL((SELECT 1 "
            + "             FROM EXP_USUARIO Z "
            + "             WHERE Z.EXP_ID = EXP.EXP_ID"
            + "             AND USU_ID  = :usuId "
            + "             AND Z.PERMISO = 2 "
            + "             and Z.IND_APROBADO = 1 "
            + "             AND Z.ACTIVO = 1),0) indIndexacion  "
            + "FROM EXPEDIENTE EXP "
            + "LEFT OUTER JOIN EXP_USUARIO EXPUSUARIO ON (EXPUSUARIO.EXP_ID = EXP.EXP_ID) "
            + "LEFT OUTER JOIN USUARIO UC ON (UC.USU_ID = EXP.USU_CREACION) "
            + "LEFT OUTER JOIN DEPENDENCIA DEP_JEFE ON (DEP_JEFE.DEP_ID = EXP.DEP_ID) "
            + "LEFT OUTER JOIN TRD TRDPRINCIPAL ON (TRDPRINCIPAL.TRD_ID = EXP.TRD_ID_PRINCIPAL) "
            + "LEFT OUTER JOIN USUARIO UJD ON (UJD.USU_ID = DEP_JEFE.USU_ID_JEFE) "
            + "WHERE ((EXP.USU_CREACION = :usuId) OR (EXPUSUARIO.USU_ID = :usuId and EXPUSUARIO.IND_APROBADO = 1 AND EXPUSUARIO.ACTIVO = 1 AND EXP.IND_CERRADO = 0) OR (DEP_JEFE.USU_ID_JEFE = :usuId)) ";
    /**
     * Consulta encargada de traer los expedientes a los que un usuario puede agregar un documento por trd
     */
    String CONSULTA_EXPEDIENTE_USUARIO_INDEXACION_TRD_ABIERTO = ""
            + "SELECT DISTINCT EXP.*"
            + "FROM EXPEDIENTE EXP "
            + "LEFT OUTER JOIN EXP_USUARIO EXPUSUARIO ON (EXPUSUARIO.EXP_ID = EXP.EXP_ID) "
            + "LEFT OUTER JOIN USUARIO UC ON (UC.USU_ID = EXP.USU_CREACION) "
            + "LEFT OUTER JOIN DEPENDENCIA DEP_JEFE ON (DEP_JEFE.DEP_ID = EXP.DEP_ID) "
            + "LEFT OUTER JOIN TRD TRDPRINCIPAL ON (TRDPRINCIPAL.TRD_ID = EXP.TRD_ID_PRINCIPAL) "
            + "LEFT OUTER JOIN USUARIO UJD ON (UJD.USU_ID = DEP_JEFE.USU_ID_JEFE) "
            + "lEFT OUTER JOIN EXP_TRD ETRD ON  (ETRD.EXP_ID = EXP.EXP_ID) "
            + "WHERE ((EXP.USU_CREACION = :usuId) OR (EXPUSUARIO.USU_ID = :usuId AND EXPUSUARIO.IND_APROBADO = 1 AND EXPUSUARIO.ACTIVO = 1 AND EXPUSUARIO.PERMISO = 2) OR (DEP_JEFE.USU_ID_JEFE = :usuId))"
            + "      AND (TRDPRINCIPAL.TRD_ID = :trdId OR (ETRD.TRD_ID = :trdId AND ETRD.ACTIVO = 1)) AND EXP.IND_APROBADO_INICIAL = 1 AND IND_CERRADO = 0";

    /**
     * Obtiene el numero de registros de expedientes por usuario.
     *
     * @param usuId Identificador del Usuario
     * @param indicador Estado del expediente cerrado
     * @return Numero de registros.
     */
    @Query(value = "select count(1)\n"
            + "from(\n"
            + CONSULTALISTAEXPEDIENTESXUSUARIO
            +" AND EXP.IND_CERRADO IN (:indicador) "
            + ") exp\n", nativeQuery = true)
    int findExpedientesDTOPorUsuarioCount(@Param("usuId") Integer usuId, @Param("indicador") List<Integer> indicador);
    
    /**
     * Obtiene el numero de registros de expedientes por usuario y filtro por el nombre
     * del expediente
     *
     * @param usuId Identificador del Usuario
     * @param filtro filtro de busqueda
     * @param indicador Estado del expediente cerrado
     * @return Numero de registros.
     */
    @Query(value = "select count(1)\n"
            + "from(\n"
            + CONSULTALISTAEXPEDIENTESXUSUARIO
            +" AND EXP.EXP_NOMBRE LIKE UPPER('%'||:filtro||'%') "
            +" AND EXP.IND_CERRADO IN (:indicador) "
            + ") exp\n", nativeQuery = true)
    int findExpedientesDTOPorUsuarioYfiltroCount(@Param("usuId") Integer usuId, @Param("filtro") String filtro, @Param("indicador") List<Integer> indicador);

    /**
     * Obtiene los registros de expedientes por usuario, de acuerdo a la fila
     * inicial y final.
     *
     * @param usuId Identificador del usuario
     * @param inicio Número de registro inicial
     * @param fin Numero de registro final
     * @param indicador Estado del expediente cerrado
     * @return Lista de bandejas de entrada.
     */
    @Query(value = ""
            + "select exp.*\n"
            + "from(\n"
            + "     select exp.*, rownum num_lineas\n"
            + "     from(\n"
            + CONSULTALISTAEXPEDIENTESXUSUARIO
            +" AND EXP.IND_CERRADO IN (:indicador) "
            + "         ORDER BY INDUSUARIOASIGNADO desc, exp.fec_creacion desc\n"
            + "     )exp\n"
            + ") exp\n"
            + "where exp.num_lineas >= :inicio and exp.num_lineas <= :fin\n", nativeQuery = true)
    List<Object[]> findExpedientesPorUsuarioPaginado(@Param("usuId") Integer usuId, @Param("inicio") int inicio, @Param("fin") int fin, @Param("indicador") List<Integer> indicador);
    
    /**
     * Obtiene los registros de expedientes por usuario y filtro, de acuerdo a la fila
     * inicial y final.
     *
     * @param usuId Identificador del usuario
     * @param inicio Número de registro inicial
     * @param fin Numero de registro final
     * @param filtro Filtro de busqueda
     * @param indicador Estado del expediente cerrado
     * @return Lista de bandejas de entrada.
     */
    @Query(value = ""
            + "select exp.*\n"
            + "from(\n"
            + "     select exp.*, rownum num_lineas\n"
            + "     from(\n"
            + CONSULTALISTAEXPEDIENTESXUSUARIO
            +" AND EXP.EXP_NOMBRE LIKE UPPER('%'||:filtro||'%')"
            +" AND EXP.IND_CERRADO IN (:indicador) "
            + "         ORDER BY INDUSUARIOASIGNADO DESC, EXP.FEC_CREACION DESC\n"
            + "     )exp\n"
            + ") exp\n"
            + "WHERE EXP.NUM_LINEAS >= :inicio AND EXP.NUM_LINEAS <= :fin\n", nativeQuery = true)
    List<Object[]> findExpedientesPorUsuarioYfiltroPaginado(@Param("usuId") Integer usuId, @Param("inicio") int inicio, @Param("fin") int fin, @Param("filtro") String filtro, @Param("indicador") List<Integer> indicador);

    /**
     * Obtiene los registros de expedientes por usuario, de acuerdo a un expediente
     * @param usuId
     * @param expId
     * @return 
     */
    @Query(value = ""
            + CONSULTALISTAEXPEDIENTESXUSUARIO
            + "AND EXP.EXP_ID = :expId", nativeQuery = true)
    List<Object[]> findExpedienteDtoPorUsuarioPorExpId(@Param("usuId") Integer usuId, @Param("expId") Long expId);
    
    /***
     * Lista los expedientes a los que un usuario puede indexar documentos dao una trd
     * @param usuId Identificador del usuario
     * @param trd identificador de la trd 
     * @return lista de los expedientes validos
     */
    @Query(value = ""
            + CONSULTA_EXPEDIENTE_USUARIO_INDEXACION_TRD_ABIERTO
            + "", nativeQuery = true)
    List<Expediente> findExpedientesIndexacionPorUsuarioPorTrd(@Param("usuId") Integer usuId, @Param("trdId") Integer trd);
}
