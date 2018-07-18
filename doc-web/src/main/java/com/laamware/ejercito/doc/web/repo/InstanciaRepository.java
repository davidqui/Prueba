package com.laamware.ejercito.doc.web.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laamware.ejercito.doc.web.entity.Instancia;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InstanciaRepository extends JpaRepository<Instancia, String> {
    
    
    @Query(nativeQuery = true, value = ""
            + "SELECT COUNT(*) FROM H_PROCESO_INSTANCIA "
            + "WHERE PIN_ID = :doc_pin_id AND PES_ID = :pes_id")
    Integer getNumeroIntanciasByPesId(@Param("doc_pin_id") String doc_pin_id, @Param("pes_id") Integer pes_id);

    /**
     * Retirna la instancia de acuerdo al id y el estado.
     * @param id El pin_id del proceso
     * @param estado El pes_id del proceso
     * @return Instancia
     */
    public Instancia findOneByIdAndEstadoId(String id, Integer estado);
}
