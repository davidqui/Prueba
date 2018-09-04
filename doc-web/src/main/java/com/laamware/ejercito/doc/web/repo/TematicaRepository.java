package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Tematica;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TematicaRepository extends JpaRepository<Tematica, Integer> {

    Tematica findOneByNombre(String nombre);

    public List<Tematica> getByActivoTrue(Sort sort);
}
