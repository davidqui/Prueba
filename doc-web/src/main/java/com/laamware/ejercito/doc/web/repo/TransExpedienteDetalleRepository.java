
package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.TransExpedienteDetalle;
import com.laamware.ejercito.doc.web.entity.TransferenciaArchivo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransExpedienteDetalleRepository extends JpaRepository<TransExpedienteDetalle, Long>{
    
    public List<TransExpedienteDetalle> findAllByTarIdAndActivo(TransferenciaArchivo transferenciaArchivo, Integer activo);


    @Query(value = "" +
    "SELECT DISTINCT TED.* \n" +
    "FROM TRANS_EXPEDIENTE_DETALLE TED\n" +
    "LEFT JOIN EXPEDIENTE EXPD ON (EXPD.EXP_ID = TED.EXP_ID)\n" +
    "WHERE EXPD.USU_CREACION = :usuID AND TED.TAR_ID != :tarId AND TED.ACTIVO = 1 AND TED.IND_REALIZADO = 0", nativeQuery = true)
    List<TransExpedienteDetalle> expedienteXUsuarioxNotTransferencia(@Param("usuID") Integer usuID, @Param("tarId") Integer tarId);
    
}
