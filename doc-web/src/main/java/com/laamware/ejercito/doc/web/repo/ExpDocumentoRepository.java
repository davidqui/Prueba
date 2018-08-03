package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.ExpDocumento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpDocumentoRepository extends JpaRepository<ExpDocumento, Long> {
    
    
    /***
     * busca el expediente documento por documento
     * @param documento
     * @return 
     */
    public ExpDocumento findByActivoTrueAndDocId(Documento documento);
}
