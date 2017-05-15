package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.laamware.ejercito.doc.web.entity.Dependencia;

public interface DependenciaRepository extends
		GenJpaRepository<Dependencia, Integer> {

	List<Dependencia> findByActivo(boolean activo, Sort sort);

	List<Dependencia> findByActivoAndPadre(boolean activo, Integer padreId,
			Sort sort);

	List<Dependencia> findByActivoAndPadreIsNull(boolean activo, Sort sort);
	
	
	Dependencia findByActivoAndDepCodigoLdap(boolean activo, String depCodLdap);

}
