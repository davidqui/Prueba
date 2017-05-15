package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import com.laamware.ejercito.doc.web.entity.Estado;

public interface EstadoRepository extends GenJpaRepository<Estado, Integer> {

	List<Estado> findByProcesoId(Integer pid);

	// 2017-02-09 jgarcia@controltechcg.com Issue #138: Corrección para
	// presentación de módulo de estados.
	List<Estado> findByProcesoIdAndActivo(Integer pid, Boolean activo);
}
