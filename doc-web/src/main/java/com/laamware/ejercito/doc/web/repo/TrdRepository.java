package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Trd;
import java.math.BigInteger;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TrdRepository extends GenJpaRepository<Trd, Integer> {

    static final Sort DEFAULT_SORT = new Sort(Direction.ASC, "nombre");

    List<Trd> findBySerie(Integer serie, Sort sort);

    List<Trd> findByActivoAndSerieNotNull(Boolean activo, Sort sort);

    List<Trd> findByActivo(boolean b, Sort sort);

    List<Trd> findByActivoAndSerieNull(boolean b, Sort sort);

    Trd findByCodigo(String ser);

    Trd findById(Integer ser);

    @Query(nativeQuery = true, value = "select distinct serie.* from DEPENDENCIA_TRD dt join trd subserie on subserie.trd_id = dt.TRD_ID join trd serie on subserie.trd_serie = serie.trd_id where dt.DEP_ID = ? order by serie.TRD_NOMBRE")
    List<Trd> findSeriesByDependencia(Integer dependenciaId);

    @Query(nativeQuery = true, value = "SELECT distinct T.* FROM TRD T JOIN DEPENDENCIA_TRD DT ON DT.TRD_ID=T.TRD_ID WHERE TRD_SERIE=? AND DT.DEP_ID = ? AND DT.TRD_ID=T.TRD_ID order by T.TRD_NOMBRE, T.TRD_CODIGO")
    List<Trd> findSubseries(Integer serieId, Integer idDependencia);

    /**
     * Valida si el usuario tiene asignada la subserie TRD dentro de una serie
     * TRD específica.
     *
     * @param subserieTrdID ID de la subserie TRD.
     * @param serieTrdID ID de la serie TRD.
     * @param usuarioID ID del usuario.
     * @return 1 en caso que el usuario tenga asignada la subserie dentro de la
     * serie especificada de forma activa; de lo contrario, 0.
     */
    /*
     * 2018-05-16 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162.
     */
    @Query(nativeQuery = true, value = ""
            + "SELECT\n"
            + "  CASE\n"
            + "    WHEN COUNT(1) > 0 THEN 1\n"
            + "    ELSE 0\n"
            + "  END\n"
            + "FROM dependencia_trd\n"
            + "JOIN trd\n"
            + "  ON (\n"
            + "  trd.trd_id = dependencia_trd.trd_id\n"
            + "  )\n"
            + "JOIN dependencia\n"
            + "  ON (\n"
            + "  dependencia.dep_id = dependencia_trd.dep_id\n"
            + "  )\n"
            + "JOIN usuario\n"
            + "  ON (\n"
            + "  usuario.dep_id = dependencia.dep_id\n"
            + "  )\n"
            + "WHERE dependencia_trd.activo = 1\n"
            + "AND trd.activo = 1\n"
            + "AND trd.trd_id = :trd_id\n"
            + "AND trd.trd_serie = :trd_serie\n"
            + "AND dependencia.activo = 1\n"
            + "AND usuario.usu_id = :usu_id"
            + "")
    public BigInteger validateSubserieTrdForUser(@Param("trd_id") Integer subserieTrdID, @Param("trd_serie") Integer serieTrdID, @Param("usu_id") Integer usuarioID);

    /**
     * Lista todas las subseries TRD activas.
     *
     * @return Lista de las subseries TRD activas.
     */
    /*
     * 2018-05-21 jgarcia@controltechcg.com Issue #170 (SICDI-Controltech)
     * feature-170.
     */
    public List<Trd> findAllByActivoTrueAndSerieNotNull();

    
            
    @Query(nativeQuery = true, value = ""
            + "SELECT DISTINCT TRD.* FROM EXP_DOCUMENTO                                                              "
            + "JOIN DOCUMENTO ON (EXP_DOCUMENTO.DOC_ID = DOCUMENTO.DOC_ID)                                  "
            + "JOIN TRD ON (DOCUMENTO.TRD_ID = TRD.TRD_ID)                                                  "
            + "WHERE EXP_DOCUMENTO.EXP_ID = :expediente AND EXP_DOCUMENTO.ACTIVO = 1                        ")
    List<Trd> getTrdsByExpedienteDocumentos(@Param("expediente") Long expediente);

    /**
     *
     * @param expId
     * @param usuId
     * @return
     */
    @Query(nativeQuery = true, value = ""
            + "SELECT *\n"
            + "FROM(\n"
            + "WITH TRD_PADRE AS\n"
            + "(SELECT TRD_ID, \n"
            + "       TRD_NOMBRE,\n"
            + "       TRD_CODIGO,\n"
            + "       CONNECT_BY_ROOT TRD_ID AS ROOT_ID,\n"
            + "       CONNECT_BY_ROOT TRD_NOMBRE AS ROOT_NOMBRE,\n"
            + "       CONNECT_BY_ROOT TRD_CODIGO AS ROOT_CODIGO\n"
            + "FROM TRD\n"
            + "WHERE ACTIVO = 1\n"
            + "START WITH TRD_SERIE IS NULL\n"
            + "CONNECT BY PRIOR TRD_ID = TRD_SERIE)\n"
            + "\n"
            + "SELECT TRD_PADRE.ROOT_ID, \n"
            + "       TRD_PADRE.ROOT_NOMBRE, \n"
            + "       TRD_PADRE.ROOT_CODIGO, \n"
            + "       USUEXP.USU_ID USUARIO, \n"
            + "       EXP.EXP_ID, COUNT(1)\n"
            + "FROM DOCUMENTO DOC\n"
            + "JOIN EXP_DOCUMENTO EXPDOC ON (EXPDOC.DOC_ID = DOC.DOC_ID AND EXPDOC.ACTIVO = 1)\n"
            + "JOIN CLASIFICACION CLADOC ON (CLADOC.CLA_ID = DOC.CLA_ID)\n"
            + "JOIN EXPEDIENTE EXP ON (EXP.EXP_ID = EXPDOC.EXP_ID)\n"
            + "JOIN TRD_PADRE ON (TRD_PADRE.TRD_ID =  DOC.TRD_ID)\n"
            + "LEFT OUTER JOIN EXP_USUARIO EXPUSU ON (EXPUSU.EXP_ID = EXP.EXP_ID AND EXPUSU.IND_APROBADO = 1 AND EXPUSU.ACTIVO = 1)\n"
            + "LEFT OUTER JOIN USUARIO USUEXP ON (USUEXP.USU_ID = EXPUSU.USU_ID AND USUEXP.ACTIVO = 1)\n"
            + "LEFT OUTER JOIN CLASIFICACION CLAUSU ON (CLAUSU.CLA_ID = USUEXP.CLA_ID)\n"
            + "GROUP BY TRD_PADRE.ROOT_ID, TRD_PADRE.ROOT_NOMBRE, TRD_PADRE.ROOT_CODIGO, USUEXP.USU_ID , EXP.EXP_ID\n"
            + "UNION\n"
            + "SELECT TRD_PADRE.ROOT_ID, \n"
            + "       TRD_PADRE.ROOT_NOMBRE,  \n"
            + "       TRD_PADRE.ROOT_CODIGO, \n"
            + "       UC.USU_ID USUARIO, \n"
            + "       EXP.EXP_ID, \n"
            + "       COUNT(1)\n"
            + "FROM DOCUMENTO DOC\n"
            + "JOIN EXP_DOCUMENTO EXPDOC ON (EXPDOC.DOC_ID = DOC.DOC_ID AND EXPDOC.ACTIVO = 1)\n"
            + "JOIN CLASIFICACION CLADOC ON (CLADOC.CLA_ID = DOC.CLA_ID)\n"
            + "JOIN EXPEDIENTE EXP ON (EXP.EXP_ID = EXPDOC.EXP_ID)\n"
            + "JOIN TRD_PADRE ON (TRD_PADRE.TRD_ID =  DOC.TRD_ID)\n"
            + "LEFT OUTER JOIN USUARIO UC ON (UC.USU_ID = EXP.USU_CREACION AND UC.ACTIVO = 1)\n"
            + "LEFT JOIN CLASIFICACION CLAUSU ON (CLAUSU.CLA_ID = UC.CLA_ID)\n"
            + "GROUP BY TRD_PADRE.ROOT_ID, TRD_PADRE.ROOT_NOMBRE, TRD_PADRE.ROOT_CODIGO, UC.USU_ID , EXP.EXP_ID\n"
            + "UNION\n"
            + "SELECT TRD_PADRE.ROOT_ID, \n"
            + "       TRD_PADRE.ROOT_NOMBRE, \n"
            + "       TRD_PADRE.ROOT_CODIGO, \n"
            + "       UJD.USU_ID USUARIO, \n"
            + "       EXP.EXP_ID, \n"
            + "       COUNT(1)\n"
            + "FROM DOCUMENTO DOC\n"
            + "JOIN EXP_DOCUMENTO EXPDOC ON (EXPDOC.DOC_ID = DOC.DOC_ID AND EXPDOC.ACTIVO = 1)\n"
            + "JOIN CLASIFICACION CLADOC ON (CLADOC.CLA_ID = DOC.CLA_ID)\n"
            + "JOIN EXPEDIENTE EXP ON (EXP.EXP_ID = EXPDOC.EXP_ID)\n"
            + "JOIN TRD_PADRE ON (TRD_PADRE.TRD_ID =  DOC.TRD_ID)\n"
            + "LEFT OUTER JOIN DEPENDENCIA DEP_JEFE ON (DEP_JEFE.DEP_ID = EXP.DEP_ID)\n"
            + "LEFT OUTER JOIN USUARIO UJD ON (UJD.USU_ID = DEP_JEFE.USU_ID_JEFE AND UJD.ACTIVO = 1)\n"
            + "LEFT JOIN CLASIFICACION CLAUSU ON (CLAUSU.CLA_ID = UJD.CLA_ID)\n"
            + "GROUP BY TRD_PADRE.ROOT_ID, TRD_PADRE.ROOT_NOMBRE, TRD_PADRE.ROOT_CODIGO, UJD.USU_ID , EXP.EXP_ID)DOC\n"
            + "WHERE DOC.EXP_ID = :expId\n"
            + "AND DOC.USUARIO  = :usuId")
    List<Object[]> getSeriesByExpedienteAndUsuario(@Param("expId") Long expId, @Param("usuId") Integer usuId);

    @Query(nativeQuery = true, value = ""
            + "SELECT *\n"
            + "FROM(\n"
            + "WITH TRD_PADRE AS\n"
            + "(SELECT TRD_ID, \n"
            + "       TRD_NOMBRE,\n"
            + "       TRD_CODIGO,\n"
            + "       CONNECT_BY_ROOT TRD_ID AS ROOT_ID,\n"
            + "       CONNECT_BY_ROOT TRD_NOMBRE AS ROOT_NOMBRE,\n"
            + "       CONNECT_BY_ROOT TRD_CODIGO AS ROOT_CODIGO\n"
            + "FROM TRD\n"
            + "WHERE ACTIVO = 1\n"
            + "START WITH TRD_SERIE IS NULL\n"
            + "CONNECT BY PRIOR TRD_ID = TRD_SERIE)\n"
            + "\n"
            + "SELECT TRD_PADRE.TRD_ID, \n"
            + "       TRD_PADRE.TRD_NOMBRE, \n"
            + "       TRD_PADRE.TRD_CODIGO, \n"
            + "       USUEXP.USU_ID USUARIO, \n"
            + "       EXP.EXP_ID, COUNT(1),\n"
            + "       TRD_PADRE.ROOT_ID\n"
            + "FROM DOCUMENTO DOC\n"
            + "JOIN EXP_DOCUMENTO EXPDOC ON (EXPDOC.DOC_ID = DOC.DOC_ID AND EXPDOC.ACTIVO = 1)\n"
            + "JOIN CLASIFICACION CLADOC ON (CLADOC.CLA_ID = DOC.CLA_ID)\n"
            + "JOIN EXPEDIENTE EXP ON (EXP.EXP_ID = EXPDOC.EXP_ID)\n"
            + "JOIN TRD_PADRE ON (TRD_PADRE.TRD_ID =  DOC.TRD_ID)\n"
            + "LEFT OUTER JOIN EXP_USUARIO EXPUSU ON (EXPUSU.EXP_ID = EXP.EXP_ID AND EXPUSU.IND_APROBADO = 1 AND EXPUSU.ACTIVO = 1)\n"
            + "LEFT OUTER JOIN USUARIO USUEXP ON (USUEXP.USU_ID = EXPUSU.USU_ID AND USUEXP.ACTIVO = 1)\n"
            + "LEFT OUTER JOIN CLASIFICACION CLAUSU ON (CLAUSU.CLA_ID = USUEXP.CLA_ID)\n"
            + "GROUP BY TRD_PADRE.TRD_ID, TRD_PADRE.TRD_NOMBRE, TRD_PADRE.TRD_CODIGO, USUEXP.USU_ID , EXP.EXP_ID, TRD_PADRE.ROOT_ID\n"
            + "UNION\n"
            + "SELECT TRD_PADRE.TRD_ID, \n"
            + "       TRD_PADRE.TRD_NOMBRE,\n"
            + "       TRD_PADRE.TRD_CODIGO, \n"
            + "       UC.USU_ID USUARIO, \n"
            + "       EXP.EXP_ID, \n"
            + "       COUNT(1),\n"
            + "       TRD_PADRE.ROOT_ID\n"
            + "FROM DOCUMENTO DOC\n"
            + "JOIN EXP_DOCUMENTO EXPDOC ON (EXPDOC.DOC_ID = DOC.DOC_ID AND EXPDOC.ACTIVO = 1)\n"
            + "JOIN CLASIFICACION CLADOC ON (CLADOC.CLA_ID = DOC.CLA_ID)\n"
            + "JOIN EXPEDIENTE EXP ON (EXP.EXP_ID = EXPDOC.EXP_ID)\n"
            + "JOIN TRD_PADRE ON (TRD_PADRE.TRD_ID =  DOC.TRD_ID)\n"
            + "LEFT OUTER JOIN USUARIO UC ON (UC.USU_ID = EXP.USU_CREACION AND UC.ACTIVO = 1)\n"
            + "LEFT JOIN CLASIFICACION CLAUSU ON (CLAUSU.CLA_ID = UC.CLA_ID)\n"
            + "GROUP BY TRD_PADRE.TRD_ID, TRD_PADRE.TRD_NOMBRE, TRD_PADRE.TRD_CODIGO, UC.USU_ID , EXP.EXP_ID, TRD_PADRE.ROOT_ID\n"
            + "UNION\n"
            + "SELECT TRD_PADRE.TRD_ID, \n"
            + "       TRD_PADRE.TRD_NOMBRE,\n"
            + "       TRD_PADRE.TRD_CODIGO, \n"
            + "       UJD.USU_ID USUARIO, \n"
            + "       EXP.EXP_ID, \n"
            + "       COUNT(1),\n"
            + "       TRD_PADRE.ROOT_ID\n"
            + "FROM DOCUMENTO DOC\n"
            + "JOIN EXP_DOCUMENTO EXPDOC ON (EXPDOC.DOC_ID = DOC.DOC_ID AND EXPDOC.ACTIVO = 1)\n"
            + "JOIN CLASIFICACION CLADOC ON (CLADOC.CLA_ID = DOC.CLA_ID)\n"
            + "JOIN EXPEDIENTE EXP ON (EXP.EXP_ID = EXPDOC.EXP_ID)\n"
            + "JOIN TRD_PADRE ON (TRD_PADRE.TRD_ID =  DOC.TRD_ID)\n"
            + "LEFT OUTER JOIN DEPENDENCIA DEP_JEFE ON (DEP_JEFE.DEP_ID = EXP.DEP_ID)\n"
            + "LEFT OUTER JOIN USUARIO UJD ON (UJD.USU_ID = DEP_JEFE.USU_ID_JEFE AND UJD.ACTIVO = 1)\n"
            + "LEFT JOIN CLASIFICACION CLAUSU ON (CLAUSU.CLA_ID = UJD.CLA_ID)\n"
            + "GROUP BY TRD_PADRE.TRD_ID, TRD_PADRE.TRD_NOMBRE, TRD_PADRE.TRD_CODIGO, UJD.USU_ID , EXP.EXP_ID, TRD_PADRE.ROOT_ID)DOC\n"
            + "WHERE DOC.EXP_ID = :expId\n"
            + "AND DOC.USUARIO  = :usuId\n"
            + "AND DOC.ROOT_ID  = :trdId\n")
    List<Object[]> getSubSerieByExpedienteAndUsuarioAndSerie(@Param("expId") Long expId, @Param("usuId") Integer usuId, @Param("trdId") Integer trdId);
}
