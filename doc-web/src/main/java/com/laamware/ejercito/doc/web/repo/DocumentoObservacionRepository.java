package com.laamware.ejercito.doc.web.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laamware.ejercito.doc.web.entity.DocumentoObservacion;

public interface DocumentoObservacionRepository extends
		JpaRepository<DocumentoObservacion, Integer> {

}
