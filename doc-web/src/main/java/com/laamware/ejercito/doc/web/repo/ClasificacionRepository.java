package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.laamware.ejercito.doc.web.entity.Clasificacion;

public interface ClasificacionRepository extends
GenJpaRepository<Clasificacion, Integer> {

	List<Clasificacion> findByActivo(boolean b, Sort sort);

}
