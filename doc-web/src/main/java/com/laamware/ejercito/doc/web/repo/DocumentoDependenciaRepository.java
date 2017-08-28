package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import com.laamware.ejercito.doc.web.entity.DocumentoDependencia;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocumentoDependenciaRepository extends GenJpaRepository<DocumentoDependencia, Integer> {

    /**
     * Obtiene los documentos archivados por dependencia y TRD, ordenados de
     * forma descendiente por la fecha de creación.
     *
     * @param depId ID de dependencia.
     * @param trd ID de la TRD.
     * @return Lista de documentos archivados.
     */
    /*
	 * 2017-05-05 jgarcia@controltechcg.com Issue #63 (SICDI-Controltech):
	 * Ordenamiento descendiente por fecha de creación del registro de archivo.
     */
    List<DocumentoDependencia> findByDependenciaIdAndTrdIdOrderByCuandoDesc(Integer depId, Integer trd);

    /**
     * Obtiene los documentos archivador por usuario y TRD, ordenados de forma
     * descendiente por la fecha de creacíón.
     *
     * @param quien ID del usuario.
     * @param trd ID de la TRD.
     * @return Lista de documentos archivados.
     */
    /*
	 * 2017-05-11 jgarcia@controltechcg.com Issue #79 (SICDI-Controltech):
	 * Limitar la presentación de documentos archivados por usuario en sesión y
	 * TRD.
     */
    List<DocumentoDependencia> findByQuienAndTrdIdOrderByCuandoDesc(Integer quien, Integer trd);

    /**
     * Obtiene todos los documentos archivados activos para un usuario.
     *
     * @param usuarioID ID del usuario.
     * @return Lista de documentos activos archivados.
     */
    /*
        2017-08-28 jgarcia@controltechcg.com Issue #120 (SICDI-Controltech) 
        feature-120: Proceso de transferencia de archivos.
     */
    @Query(nativeQuery = true, value = ""
            + "SELECT \n"
            + "    * \n"
            + "FROM \n"
            + "    DOCUMENTO_DEPENDENCIA \n"
            + "WHERE \n"
            + "        ACTIVO = 1 \n"
            + "    AND \n"
            + "        QUIEN =:usuarioID"
            + "")
    List<DocumentoDependencia> findAllActivoByUsuario(@Param("usuarioID") Integer usuarioID);

    /**
     * Obtiene todos los documentos archivados para un usuario, asociados a un
     * proceso de transferencia de archivo.
     *
     * @param usuarioID ID del usuario.
     * @param transferenciaArchivoID ID de la transferencia de archivo.
     * @return Lista de documentos archivados.
     */
    /*
        2017-08-28 jgarcia@controltechcg.com Issue #120 (SICDI-Controltech) 
        feature-120: Proceso de transferencia de archivos.
     */
    @Query(nativeQuery = true, value = ""
            + "SELECT \n"
            + "    DOCUMENTO_DEPENDENCIA.* \n"
            + "FROM \n"
            + "    DOCUMENTO_DEPENDENCIA \n"
            + "    JOIN TRANSFERENCIA_ARCHIVO_DETALLE ON ( \n"
            + "        TRANSFERENCIA_ARCHIVO_DETALLE.DCDP_ID = DOCUMENTO_DEPENDENCIA.DCDP_ID \n"
            + "    ) \n"
            + "WHERE \n"
            + "        DOCUMENTO_DEPENDENCIA.QUIEN =:usuarioID \n"
            + "    AND \n"
            + "        TRANSFERENCIA_ARCHIVO_DETALLE.TAR_ID = transferenciaArchivoID"
            + "")
    List<DocumentoDependencia> findAllByUsuarioAndTransferenciaArchivo(
            @Param("usuarioID") Integer usuarioID,
            @Param("transferenciaArchivoID") Integer transferenciaArchivoID);

}
