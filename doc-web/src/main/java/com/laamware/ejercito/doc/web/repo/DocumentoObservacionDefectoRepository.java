package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.DocumentoObservacionDefecto;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de persistencia para {@link DocumentoObservacionDefecto}.
 *
 * @author samuel.delgado@controltechcg.com
 * @since 1.8
 * @version 05/23/2018 Issue #172 (SICDI-Controltech) feature-172
 */
@Repository
public interface DocumentoObservacionDefectoRepository extends JpaRepository<DocumentoObservacionDefecto, Integer> {
    
    /**
     * Lista todas las observaciones activas
     * @param sort
     * @return 
     */
    List<DocumentoObservacionDefecto> getByActivoTrue(Sort sort);

    /**
     * Lista todas las observaciones por defecto activas, ordenadas por el
     * texto.
     *
     * @return Lista de observaciones activas ordenadas por texto.
     */
    public List<DocumentoObservacionDefecto> findAllByActivoTrueOrderByTextoObservacionAsc();

}
