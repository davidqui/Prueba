package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.ExpedienteTransicion;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpedienteTransicionRepository extends JpaRepository<ExpedienteTransicion, Long> {

    public List<ExpedienteTransicion> findByExpIdExpId(Long expId, Sort sort);
    
    public List<ExpedienteTransicion> findByDocId(Documento documento, Sort sort);
}
