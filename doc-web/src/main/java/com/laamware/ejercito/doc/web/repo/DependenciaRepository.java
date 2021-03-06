package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.Usuario;

public interface DependenciaRepository extends GenJpaRepository<Dependencia, Integer> {

    List<Dependencia> findByActivo(boolean activo, Sort sort);

    List<Dependencia> findByActivoAndPadre(boolean activo, Integer padreId, Sort sort);

    List<Dependencia> findByActivoAndPadreIsNull(boolean activo, Sort sort);

    Dependencia findByActivoAndDepCodigoLdap(boolean activo, String depCodLdap);

    /**
     * Obtiene la lista de dependencias activas en las cuales el usuario es jefe
     * principal o jefe encargado.
     *
     * @param usuarioId ID del usuario.
     * @return Lista de dependencias activas.
     */
    // 2017-06-01 jgarcia@controltechcg.com Issue #99 (SICDI-Controltech)
    // hotfix-99
    @Query(nativeQuery = true, value = ""
            + " SELECT                                                                           "
            + " DEPENDENCIA.*                                                                    "
            + " FROM                                                                             "
            + " DEPENDENCIA                                                                      "
            + " WHERE                                                                            "
            + " DEPENDENCIA.ACTIVO = 1                                                           "
            + " AND (DEPENDENCIA.USU_ID_JEFE = :usuarioId OR DEPENDENCIA.USU_ID_JEFE = :usuarioId ) ")
    List<Dependencia> findActivoByJefeAsignado(@Param("usuarioId") Integer usuarioId);

    /**
     * Obtiene el ID de la dependencia unidad.
     *
     * @param dependenciaID ID de la dependencia.
     * @return ID de la unidad.
     */
    /*
     * 2017-09-18 jgarcia@controltechcg.com Issue #120 (SICDI-Controltech)
     * feature-120: Campos para acta de transferencia.
     */
    @Query(nativeQuery = true, value = ""
            + "select ROOT_KEY \n"
            + "FROM("
            + "     SELECT rownum row_num, CONNECT_BY_ROOT DEP_ID AS ROOT_KEY \n"
            + "     FROM DEPENDENCIA \n"
            + "     WHERE DEP_ID = :dependenciaID \n"
            + "     and (CONNECT_BY_ROOT DEP_IND_ENVIO_DOCUMENTOS = 1 or CONNECT_BY_ROOT dep_padre is null) \n"
            + "     CONNECT BY DEP_PADRE = PRIOR DEP_ID \n"
            + ") \n"
            + "where row_num = 1")
    public Integer findUnidadID(@Param("dependenciaID") Integer dependenciaID);

    /*
    * 2018-02-14 edison.gonzalez@controltechcg.com issue #150
    * (SICDI-Controltech) hotfix-150: Ajuste para desplegar visualmente  las 
    * unidades padres en el arbol, cuando se reasigna un documento.
    */
    List<Dependencia> findByActivoAndPadreAndDepIndEnvioDocumentos(boolean activo, Integer padreId, boolean indUnidadPadre, Sort sort);

    /*
    * 2018-08-17 samuel.delgado@controltechcg.com issue #7 gogs
    * (SICDI-Controltech) feature-gogs-7: Lista las dependencias que tengan un 
    *  usuario registro dado por un usuario.
    */
    public List<Dependencia> findActivoByUsuarioRegistro(Usuario usuario);
}
