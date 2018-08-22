package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.RazonInhabilitar;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de persistencia para {@link razonInhabilitar}.
 *
 * @author samuel.delgado@controltechcg.com
 * @since 1.8
 * @version 08/16/2018 Issue gogs #7 (SICDI-Controltech) feature-gogs-7
 */
public interface RazonInhabilitarRepository extends JpaRepository<RazonInhabilitar, Integer>{
    
    /**
     * Lista todas las razones para inhabilitar usuarios
     * @param sort
     * @return 
     */
    List<RazonInhabilitar> getByActivoTrue(Sort sort);

    /**
     * Lista todas las razones para inhabilitar usuarios activas, ordenadas por el
     * texto.
     *
     * @return Lista de observaciones activas ordenadas por texto.
     */
    public List<RazonInhabilitar> findAllByActivoTrueOrderByTextoRazonAsc();

}
