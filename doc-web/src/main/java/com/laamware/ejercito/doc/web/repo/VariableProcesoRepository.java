package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import com.laamware.ejercito.doc.web.entity.VariableProceso;

public interface VariableProcesoRepository extends
GenJpaRepository<VariableProceso, Integer> {

	List<VariableProceso> findByProcesoId(Integer pid);

}
