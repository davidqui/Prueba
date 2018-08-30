
package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.TransferenciaArchivo;
import com.laamware.ejercito.doc.web.entity.TransferenciaObservacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferenciaObservacionRepository extends JpaRepository<TransferenciaObservacion, Long>{
    
    /**
     * Lista las observaciones de una transferencia
     * @param tarId transferencia
     * @return lista de observaciones
     */
    public List<TransferenciaObservacion> findByTarIdOrderByFecCreacionDesc(TransferenciaArchivo tarId);
}
