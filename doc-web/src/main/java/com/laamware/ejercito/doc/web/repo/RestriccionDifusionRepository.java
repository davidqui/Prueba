package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.RestriccionDifusion;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Sort;

/**
 *  Interface que permite realizar las consultas sobre la entidad 
 *  RestriccionDifusion
 * @since Sep 29, 2017
 * @version 1.0.0 (feature-129).
 * @author egonzalezm
 */
public interface RestriccionDifusionRepository extends GenJpaRepository<RestriccionDifusion, BigDecimal>{
    
    /**
     * Busca las restricciones de difusion.
     *
     * @param sort Parametro para identificar el tipo de ordenamiento
     * @return RestriccionDifusion Restricciones de difusion.
     */
    /*
     * 2017-09-29 edison.gonzalez@controltechcg.com Issue #129 (SICDI-Controltech) 
     * feature-129: Presentar las restricciones de difusión.
     */
    List<RestriccionDifusion> findByActivoTrue(Sort sort);
}
