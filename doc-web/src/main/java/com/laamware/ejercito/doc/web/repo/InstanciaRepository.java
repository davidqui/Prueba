package com.laamware.ejercito.doc.web.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laamware.ejercito.doc.web.entity.Instancia;

public interface InstanciaRepository extends JpaRepository<Instancia, String> {

    /**
     * Retirna la instancia de acuerdo al id y el estado.
     * @param id El pin_id del proceso
     * @param estado El pes_id del proceso
     * @return Instancia
     */
    public Instancia findOneByIdAndEstadoId(String id, Integer estado);
}
