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
    
    
    
    public List<TransferenciaArchivo> findAllByOrigenUsuarioId(Integer usuId);

}
