
package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.TransJustificacionDefecto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransJustificacionDefectoRepository extends JpaRepository<TransJustificacionDefecto, Long>{
    
    /**
     * Lista todas las observaciones activas
     * @param sort
     * @return 
     */
    List<TransJustificacionDefecto> getByActivoTrue(Sort sort);

    public TransJustificacionDefecto findOneByTextoObservacionAndActivoTrue(String textoObservacion);

    public List<TransJustificacionDefecto> findAllByActivoTrueOrderByTextoObservacionAsc();
    /**
    * 2018-09-24 samuel.delgado@controltechcg.com Issue #174 (SICDI-Controltech)
    * feature-174: Adición para la paginación.
    */
    public Page<TransJustificacionDefecto> findAllByActivoTrue(Pageable pageable);
    
}
