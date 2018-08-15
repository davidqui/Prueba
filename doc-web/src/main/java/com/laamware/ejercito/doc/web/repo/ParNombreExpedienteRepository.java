package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.ParNombreExpediente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParNombreExpedienteRepository extends JpaRepository<ParNombreExpediente, Long> {

    ParNombreExpediente findOneByParNombre(String nopmbre);
}
