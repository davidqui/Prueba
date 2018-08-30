
package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.TransferenciaArchivo;
import com.laamware.ejercito.doc.web.entity.TransferenciaEstado;
import com.laamware.ejercito.doc.web.entity.TransferenciaTransicion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferenciaTransicionRepository extends JpaRepository<TransferenciaTransicion, Long>{
    
    public List<TransferenciaTransicion> findOneByTraEstIdAndTarId(TransferenciaEstado transferenciaEstado, TransferenciaArchivo transferenciaArchivo);
    
    public List<TransferenciaTransicion> findByTarIdOrderByFecCreacionDesc(TransferenciaArchivo transferenciaArchivo);
}
