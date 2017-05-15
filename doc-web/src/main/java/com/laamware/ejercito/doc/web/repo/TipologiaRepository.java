package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.laamware.ejercito.doc.web.entity.Tipologia;

public interface TipologiaRepository extends GenJpaRepository<Tipologia, Integer> {

	List<Tipologia> findByActivo(boolean b, Sort sort);

	List<Tipologia> findByActivoAndCodigoStartingWith(boolean b, String codigo);

	List<Tipologia> findByActivoAndCodigoStartingWithOrCodigoStartingWithOrCodigoStartingWith(
			boolean b, String subserie, String string, String string2);

}
