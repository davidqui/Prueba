package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.EstadoExpediente;

public interface EstadoExpedienteRepository extends
		GenJpaRepository<EstadoExpediente, Integer> {

	EstadoExpediente getByNombre(String name);

}
