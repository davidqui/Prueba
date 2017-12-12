package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Documento;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author egonzalezm
 */
public interface ReporteRepository extends JpaRepository<Documento, String> {

    @Query(nativeQuery = true, value = ""
            + "WITH UNIDAD_DEPENDENCIA AS\n"
            + "  (SELECT DEP_ID,\n"
            + "    CONNECT_BY_ROOT DEP_ID AS ROOT_ID,\n"
            + "    CONNECT_BY_ROOT DEP_SIGLA AS ROOT_SIGLA,\n"
            + "    CONNECT_BY_ROOT DEP_NOMBRE AS ROOT_NOMBRE\n"
            + "  FROM DEPENDENCIA\n"
            + "  WHERE ACTIVO            = 1\n"
            + "    START WITH DEP_PADRE_ORGANICO = :dependencia\n"
            + "    CONNECT BY DEP_PADRE_ORGANICO  = PRIOR DEP_ID\n"
            + "  union \n"
            + "   select DEP_ID, DEP_ID, DEP_SIGLA, DEP_NOMBRE\n"
            + "   from DEPENDENCIA\n"
            + "   where dep_id = :dependencia"
            + "  )\n"
            + "SELECT ROOT_SIGLA, ROOT_NOMBRE, COUNT(1)\n"
            + "FROM DOCUMENTO\n"
            + "JOIN USUARIO ON (USUARIO.USU_ID = DOCUMENTO.USU_ID_ELABORA) \n"
            + "JOIN UNIDAD_DEPENDENCIA\n"
            + "ON (UNIDAD_DEPENDENCIA.DEP_ID = USUARIO.DEP_ID)\n"
            + "WHERE DOCUMENTO.DEP_ID_DES IS NOT NULL\n"
            + "AND DOCUMENTO.DOC_RADICADO IS NOT NULL\n"
            + "AND DOCUMENTO.CUANDO BETWEEN :fechaInicial AND :fechaFinal\n"
            + "GROUP BY ROOT_SIGLA, ROOT_NOMBRE\n"
            + "ORDER BY COUNT(1) ASC")
    List<Object[]> findReporteDependencia(@Param("dependencia") Integer dependencia, @Param("fechaInicial") Date fechaInicial, @Param("fechaFinal") Date fechaFinal);

    @Query(nativeQuery = true, value = ""
            + "WITH UNIDAD_DEPENDENCIA AS\n"
            + "  (SELECT DEP_ID,\n"
            + "    CONNECT_BY_ROOT DEP_ID AS ROOT_ID,\n"
            + "    CONNECT_BY_ROOT DEP_SIGLA AS ROOT_SIGLA,\n"
            + "    CONNECT_BY_ROOT DEP_NOMBRE AS ROOT_NOMBRE\n"
            + "  FROM DEPENDENCIA\n"
            + "  WHERE ACTIVO = 1\n"
            + "    START WITH DEP_PADRE_ORGANICO = :dependencia\n"
            + "    CONNECT BY DEP_PADRE_ORGANICO  = PRIOR DEP_ID\n"
            + "  union \n"
            + "   select DEP_ID, DEP_ID, DEP_SIGLA, DEP_NOMBRE\n"
            + "   from DEPENDENCIA\n"
            + "   where dep_id = :dependencia"
            + "  )\n"
            + "SELECT TRD_NOMBRE, COUNT(1)\n"
            + "FROM DOCUMENTO\n"
            + "JOIN USUARIO ON (USUARIO.USU_ID = DOCUMENTO.USU_ID_ELABORA) \n"
            + "JOIN UNIDAD_DEPENDENCIA ON (UNIDAD_DEPENDENCIA.DEP_ID = USUARIO.DEP_ID)\n"
            + "JOIN TRD ON (TRD.TRD_ID = DOCUMENTO.TRD_ID)\n"
            + "WHERE DOCUMENTO.DEP_ID_DES IS NOT NULL \n"
            + "AND DOCUMENTO.DOC_RADICADO IS NOT NULL\n"
            + "AND DOCUMENTO.CUANDO BETWEEN :fechaInicial AND :fechaFinal\n"
            + "GROUP BY TRD_NOMBRE\n"
            + "ORDER BY COUNT(1) ASC")
    List<Object[]> findReporteDependenciaXtrd(@Param("dependencia") Integer dependencia, @Param("fechaInicial") Date fechaInicial, @Param("fechaFinal") Date fechaFinal);
}