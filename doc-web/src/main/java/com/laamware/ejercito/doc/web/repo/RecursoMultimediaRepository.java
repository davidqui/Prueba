package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.RecursoMultimedia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecursoMultimediaRepository extends JpaRepository<RecursoMultimedia, Integer> {

    RecursoMultimedia findOneByNombre(String nombre);
}
