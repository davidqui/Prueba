package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.laamware.ejercito.doc.web.entity.HProcesoInstancia;

public interface HProcesoInstanciaRepository extends
		JpaRepository<HProcesoInstancia, Integer> {

	List<HProcesoInstancia> findById(String id, Sort sort);

}
