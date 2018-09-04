package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Tematica;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TematicaRepository extends JpaRepository<Tematica, Integer> {

    Tematica findOneByNombre(String nombre);
}
