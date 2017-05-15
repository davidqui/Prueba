package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import com.laamware.ejercito.doc.web.entity.Formato;

public interface FormatoRepository extends GenJpaRepository<Formato, Integer> {

	List<Formato> getFormatoByDocumentoId(String docId);

	List<Formato> findByTrdId(Integer subId);

	Formato findByContenido(String id);

}
