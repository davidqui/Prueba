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
     * Consulta que se encarga de listar las transferencias por un usuario origen
     */
    String CONSULTALISTATRANSFERENCIAXUSUARIOORIGEN = ""
            + "SELECT * "
            + "from TRANSFERENCIA_ARCHIVO "
            + "WHERE ORIGEN_USU_ID = :usuId "
            + "and num_documentos > 0";
    
    /**
     * Obtiene el numero de registros de transferencias por usuario de origen.
     *
     * @param usuId Identificador del Usuario
     * @return Numero de registros.
     */
    @Query(value = "select count(1)\n"
            + "from(\n"
            + CONSULTALISTATRANSFERENCIAXUSUARIOORIGEN
            + ") ta\n", nativeQuery = true)
    int findCountByOrigenUsuarioId(@Param("usuId") Integer usuId);
    
    
    /**
     * Obtiene los registros dde transferencias por usuario de origen, de acuerdo a la fila
     * inicial y final.
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
            + CONSULTALISTATRANSFERENCIAXUSUARIOORIGEN
            + "     ORDER BY FECHA_CREACION desc\n"
            + "     )ta\n"
            + ") ta\n"
            + "where ta.num_lineas >= :inicio and ta.num_lineas <= :fin\n", nativeQuery = true)
    List<TransferenciaArchivo> findAllByOrigenUsuarioId(@Param("usuId") Integer usuId, @Param("inicio") int inicio, @Param("fin") int fin);
}
