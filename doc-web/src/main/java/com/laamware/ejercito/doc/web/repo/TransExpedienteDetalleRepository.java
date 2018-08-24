
package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.TransExpedienteDetalle;
import com.laamware.ejercito.doc.web.entity.TransferenciaArchivo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransExpedienteDetalleRepository extends JpaRepository<TransExpedienteDetalle, Long>{
    
    public List<TransExpedienteDetalle> findAllByTarIdAndActivoTrue(TransferenciaArchivo transferenciaArchivo);
}
