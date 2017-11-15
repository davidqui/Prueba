package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Proceso;
import com.laamware.ejercito.doc.web.entity.Radicacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RadicacionRepository extends JpaRepository<Radicacion, Integer> {

    Radicacion findByProceso(Proceso proceso);
}
