package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.ExpedienteTransicion;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpedienteTransicionRepository extends JpaRepository<ExpedienteTransicion, Long> {

    /**
     * Lista las transiciones dado un expediente
     * @param expId Identificador de la transición
     * @param sort ordernador
     * @return lista de transiciones
     */
    public List<ExpedienteTransicion> findByExpIdExpId(Long expId, Sort sort);
    
    /***
     * Lista las transiciones dado un documento
     * @param documento documento a consultar
     * @param sort ordernador
     * @return lista de transiciones
     */
    public List<ExpedienteTransicion> findByDocId(Documento documento, Sort sort);
}
