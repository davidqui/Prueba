package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import com.laamware.ejercito.doc.web.entity.Estado;
import com.laamware.ejercito.doc.web.entity.Proceso;
import com.laamware.ejercito.doc.web.entity.Transicion;

public interface TransicionRepository extends
		GenJpaRepository<Transicion, Integer> {

	List<Transicion> findByEstadoInicial(Estado procesoEstado);

	List<Transicion> findByEstadoInicialProceso(Proceso proceso);

	List<Transicion> findByActivo(boolean activo);

	List<Transicion> findByEstadoInicialAndActivo(Estado estadoInicial,
			boolean b);

}
