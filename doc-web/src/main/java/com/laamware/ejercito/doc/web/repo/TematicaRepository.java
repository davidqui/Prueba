package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.RecursoMultimedia;
import com.laamware.ejercito.doc.web.entity.Tematica;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TematicaRepository extends JpaRepository<Tematica, Integer> {

    Tematica findOneByNombre(String nombre);

    public List<Tematica> getByActivoTrue(Sort sort);
    public Page<Tematica> getByActivoTrue(Pageable pageable);
    @Override
    public Page<Tematica> findAll(Pageable pageable);
}
