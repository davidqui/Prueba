package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.PlantillaFuidGestion;

/**
 * Repositorio de persistencia para {@link PlantillaFuidGestion}.
 *
 * @author edison.gonzalez@controltechcg.com
 * @since Sep 11, 2018
 * @version 1.0.0 (feature-gogs-4).
 */
public interface PlantillaFuidGestionRepository extends GenJpaRepository<PlantillaFuidGestion, Integer> {

    /**
     * Busca el registro de plantilla activa.
     *
     * @return Registro de plantilla.
     */
    PlantillaFuidGestion findByActivoTrue();
}
