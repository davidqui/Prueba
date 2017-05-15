package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.laamware.ejercito.doc.web.entity.Grados;


public interface GradosRepository extends
GenJpaRepository<Grados, String>{
	
	List<Grados> findByActivo(boolean activo, Sort sort);
	
	Grados findByActivoAndId(boolean activo, String id, Sort sort);
	
	//List<Grados> findByActivoAndSigla(boolean activo, String sigla,Sort sort);
}
