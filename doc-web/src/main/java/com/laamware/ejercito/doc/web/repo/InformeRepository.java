package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.laamware.ejercito.doc.web.entity.Informe;

public interface InformeRepository extends GenJpaRepository<Informe, Integer> {

	List<Informe> findByActivo(boolean b, Sort sort);

}
