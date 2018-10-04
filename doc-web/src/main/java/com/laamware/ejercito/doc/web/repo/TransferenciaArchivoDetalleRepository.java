package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.TransferenciaArchivo;
import com.laamware.ejercito.doc.web.entity.TransferenciaArchivoDetalle;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositorio de persistencia para {@link TransferenciaArchivoDetalle}.
 *
 * @author jgarcia@controltechcg.com
 * @since Ago 25, 2017
 * @version 1.0.0 (feature-120).
 */
public interface TransferenciaArchivoDetalleRepository extends JpaRepository<TransferenciaArchivoDetalle, Integer> {

    /**
     * Busca los detalles de una transferencia de archivo.
     *
     * @param transferenciaArchivo Transferencia de archivo.
     * @return Lista de detalles.
     */
    public List<TransferenciaArchivoDetalle> findAllByTransferenciaArchivoAndActivo(TransferenciaArchivo transferenciaArchivo, int activo);
    
    /**
     * Busca los detalles de una transferencia arch8ivo que se encuentran en expedientes del usuario.
     * @param usuId identificador del usuario
     * @param transId identificador de la transferencia
     * @return lista detalles
     */
    @Query(value = "" +
            "SELECT TAD.* \n" +
            "        FROM TRANSFERENCIA_ARCHIVO_DETALLE TAD\n" +
            "        LEFT JOIN EXP_DOCUMENTO EXD ON (EXD.DOC_ID = TAD.DOC_ID AND EXD.ACTIVO = 1)\n" +
            "        LEFT JOIN EXPEDIENTE EXP ON (EXP.EXP_ID = EXD.EXP_ID)\n" +
            "        WHERE TAD.ACTIVO = 1 AND TAD.IND_REALIZADO = 0 AND TAD.TAR_ID = :transId AND EXP.USU_CREACION = :usuId", nativeQuery = true)
    public List<TransferenciaArchivoDetalle> findAllTransferenciaExpedienteExpId(@Param("usuId") Integer usuId, @Param("transId") Integer transId);
}
