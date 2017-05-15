package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import com.laamware.ejercito.doc.web.entity.Condicion;
import com.laamware.ejercito.doc.web.entity.Transicion;

public interface CondicionRepository extends
		GenJpaRepository<Condicion, Integer> {

	List<Condicion> findByTransicion(Transicion transicion);

	List<Condicion> findByTransicionAndActivo(Transicion transicion, Boolean b);

}
