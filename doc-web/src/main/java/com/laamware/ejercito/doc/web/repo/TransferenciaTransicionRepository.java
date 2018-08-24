
package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.TransferenciaArchivo;
import com.laamware.ejercito.doc.web.entity.TransferenciaEstado;
import com.laamware.ejercito.doc.web.entity.TransferenciaTransicion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferenciaTransicionRepository extends JpaRepository<TransferenciaTransicion, Long>{
    
    public TransferenciaTransicion findOneByTraEstIdAndTarId(TransferenciaEstado transferenciaEstado, TransferenciaArchivo transferenciaArchivo);
}
