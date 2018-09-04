package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.RecursoMultimedia;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecursoMultimediaRepository extends JpaRepository<RecursoMultimedia, Integer> {

    RecursoMultimedia findOneByNombreAndActivoTrue(String nombre);

    public List<RecursoMultimedia> getByActivoTrue(Sort sort);
}
