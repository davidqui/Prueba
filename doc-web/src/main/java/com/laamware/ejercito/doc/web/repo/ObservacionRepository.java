package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laamware.ejercito.doc.web.entity.Observacion;

public interface ObservacionRepository extends
		JpaRepository<Observacion, Integer> {

	List<Observacion> findByInstanciaId(String pin);

}
