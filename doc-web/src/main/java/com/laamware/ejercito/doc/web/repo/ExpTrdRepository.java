package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.ExpTrd;
import com.laamware.ejercito.doc.web.entity.Expediente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpTrdRepository extends JpaRepository<ExpTrd, Long> {
    
    /**
     * Lista todas las trds de un expediente.
     * @param expediente
     * @return 
     */
    List<ExpTrd> findByExpIdAndActivoTrue(Expediente expediente);
    
    /**
     * Lista todas las trds de un expediente 
     * @param expediente expediente a consultar
     * @return lista de trds 
     */
    List<ExpTrd> findByExpId(Expediente expediente);
}
